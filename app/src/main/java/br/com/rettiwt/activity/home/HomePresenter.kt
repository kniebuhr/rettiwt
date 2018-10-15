package br.com.rettiwt.activity.home

import br.com.rettiwt.*
import br.com.rettiwt.models.*
import com.google.gson.Gson
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread


class HomePresenter : HomeContract.Presenter {

    private val socketListener = object : AsyncSocket.SocketListener {
        override fun messageReceived(message: String) {
            message.parse { method, message ->
                val gson = Gson()
                when (method) {
                    METHOD_NEW_STAR -> {
                        val response = gson.fromJson(message, UpdatePostResponse::class.java)
                        updateStarCount(response)
                    }
                    METHOD_NEW_RETTIWT -> {
                        val response = gson.fromJson(message, UpdatePostResponse::class.java)
                        updateRettiwtCount(response)
                    }
                    METHOD_NEW_POST -> {
                        val response = gson.fromJson(message, NewPostResponse::class.java)
                        updateItems(listOf(response.data!!.toModel()), false)
                    }
                    METHOD_GET_ALL -> {
                        val response = gson.fromJson(message, AllPostsResponse::class.java)
                        updateItems(response.data!!.map { it.toModel() }, true)
                    }
                }
            }
        }

        override fun onError(status: Int?, message: String?) {
            view?.displayMessage(message)
            if (status == 1401) {
                logout(true)
            }
        }
    }

    private var view: HomeContract.View? = null
    private var homeItems: List<HomeItemModel> = listOf()

    override fun loadData() {
        AsyncSocket.socketListener = socketListener
        AsyncSocket.send(METHOD_GET_ALL)
    }

    private fun updateItems(posts: List<HomeItemModel>, allPosts: Boolean) {
        if (allPosts) {
            homeItems = posts
        } else {
            homeItems = posts + homeItems
            view?.displayNewContentButton(true)
        }
        view?.displayItems(homeItems)
    }

    private fun updateRettiwtCount(response: UpdatePostResponse) {
        homeItems.forEach {
            if (it.id == response.data?.post_id)
                it.rettiwts = response.data?.count
        }
        view?.displayItems(homeItems)
    }

    private fun updateStarCount(response: UpdatePostResponse) {
        homeItems.forEach {
            if (it.id == response.data?.post_id)
                it.stars = response.data?.count
        }
        view?.displayItems(homeItems)
    }

    override fun onClickLogout() {
        logout(false)
    }

    private fun logout(invalid: Boolean) {
        PreferencesHelper.putAuthorization(null)
        view?.openLogin(invalid)
    }

    override fun onClickClearDb() {
        AsyncSocket.send(METHOD_CLEAR, null)
    }

    override fun onClickConfig() {
        view?.openConfig()
    }

    override fun onClickSend(message: String) {
        AsyncSocket.send(METHOD_SEND, SendParams(message))
    }

    override fun onClickStar(id: String?) {
        AsyncSocket.send(METHOD_STAR, StarParams(id))
    }

    override fun onClickRettiwt(id: String?) {
        AsyncSocket.send(METHOD_RETTIWT, RettiwtParams(id))
    }

    override fun onClickCamera() {
        view?.openGalleryPicker()
    }

    override fun onImagePicked(text: String?, bitmap: Bitmap?) {
        thread {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
            AsyncSocket.send(METHOD_SEND, SendParams(text, "Imagem"))
        }
    }

    override fun attachView(view: HomeContract.View) {
        this.view = view
    }

    override fun detachView() {
        AsyncSocket.socketListener = null
        view = null
    }
}