package br.com.rettiwt.activity.home

import android.graphics.Bitmap
import android.net.Uri
import br.com.rettiwt.models.HomeItemModel

interface HomeContract {

    interface View {
        fun displayMessage(msg: String?)
        fun displayNewContentButton(show: Boolean)
        fun displayItems(items: List<HomeItemModel>)
        fun openConfig()
        fun openGalleryPicker()
        fun openLogin(invalid: Boolean)
    }

    interface Presenter {
        fun loadData()
        fun onClickLogout()
        fun onClickClearDb()
        fun onClickConfig()
        fun onClickStar(id: String?)
        fun onClickRettiwt(id: String?)
        fun onClickSend(message: String)
        fun onClickCamera()
        fun onImagePicked(text: String?, bitmap: Bitmap?)
        fun attachView(view: View)
        fun detachView()
    }
}