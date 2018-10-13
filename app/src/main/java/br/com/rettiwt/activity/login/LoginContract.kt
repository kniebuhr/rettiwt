package br.com.rettiwt.activity.login

interface LoginContract {

    interface View {
        fun displayMessage(msg: String?)
        fun openHome()
    }

    interface Presenter {
        fun onClickEnter(username: String, password: String)
        fun attachView(view: View)
        fun detachView()
    }

}