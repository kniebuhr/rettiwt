package br.com.rettiwt.activity.home

import android.graphics.Bitmap
import android.net.Uri
import br.com.rettiwt.models.HomeItemModel

interface HomeContract {

    interface View {
        fun displayMessage(msg: String?)
        fun displaySnack(action: () -> Unit)
        fun displayNewContentButton(show: Boolean)
        fun displayItems(items: List<HomeItemModel>)
        fun openGalleryPicker()
        fun openLogin(invalid: Boolean)
    }

    interface Presenter {
        fun startSocket()
        fun onClickLogout()
        fun onClickStar(id: String?)
        fun onClickRettiwt(id: String?)
        fun onClickSend(message: String)
        fun onClickCamera()
        fun onImagePicked(text: String?, bitmap: Bitmap?)
        fun attachView(view: View)
        fun detachView()
    }
}