package br.com.rettiwt.activity.home

import android.net.Uri
import br.com.rettiwt.*
import br.com.rettiwt.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomePresenter : HomeContract.Presenter {

    private val socketListener = object : AsyncSocket.SocketListener {
        override fun messageReceived(message: String) {
            message.parse { method, message ->
                val gson = Gson()
                when (method) {
                    METHOD_NEW_POST -> {
                        val response = gson.fromJson(message, PostResponse::class.java)
                        updateItems(listOf(response.toModel()))
                    }
                    METHOD_GET_ALL -> {
                        val type = object : TypeToken<List<PostResponse>>() {}.type
                        val response = Gson().fromJson<List<PostResponse>>(message, type)
                        updateItems(response.map { it.toModel() })
                    }
                }
            }
        }

        override fun onError(message: String?) {
            view?.displayMessage(message)
        }
    }

    private var view: HomeContract.View? = null
    private val homeItems: MutableList<HomeItemModel> = mutableListOf()

    override fun startSocket() {
        AsyncSocket.socketListener = socketListener
        AsyncSocket.send(MethodRequest(METHOD_GET_ALL, APP_ID, PreferencesHelper.getAuthorization(), null))
    }

    private fun updateItems(posts: List<HomeItemModel>) {
        homeItems.addAll(posts)
        view?.displayItems(homeItems)
    }

    override fun onClickSend(message: String) {
        AsyncSocket.send(MethodRequest(METHOD_SEND, APP_ID, PreferencesHelper.getAuthorization(), SendParams(message)))
    }

    override fun onClickStar(id: String?) {
        AsyncSocket.send(MethodRequest(METHOD_STAR, APP_ID, PreferencesHelper.getAuthorization(), StarParams(id)))
    }

    override fun onClickRettiwt(id: String?) {
        AsyncSocket.send(MethodRequest(METHOD_RETTIWT, APP_ID, PreferencesHelper.getAuthorization(), RettiwtParams(id)))
    }

    override fun onClickCamera() {
        view?.openGalleryPicker()
    }

    override fun onImagePicked(imageUri: Uri?) {

    }

    override fun attachView(view: HomeContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}