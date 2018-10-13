package br.com.rettiwt.activity.home

import android.net.Uri
import br.com.rettiwt.models.HomeItemModel

interface HomeContract {

    interface View {
        fun displayMessage(msg: String?)
        fun displaySnack(action: () -> Unit)
        fun displayNewContentButton(show: Boolean)
        fun displayItems(items: List<HomeItemModel>)
        fun openGalleryPicker()
    }

    interface Presenter {
        fun startSocket()
        fun onClickStar(id: String?)
        fun onClickRettiwt(id: String?)
        fun onClickSend(message: String)
        fun onClickCamera()
        fun onImagePicked(imageUri: Uri?)
        fun attachView(view: View)
        fun detachView()
    }
}