package br.com.rettiwt.activity.login

interface LoginContract {

    interface View {
        fun displayMessage(msg: String?)
        fun openHome()
        fun openConfig()
    }

    interface Presenter {
        fun onClickEnter(username: String, password: String)
        fun onClickConfig()
        fun attachView(view: View)
        fun detachView()
    }

}