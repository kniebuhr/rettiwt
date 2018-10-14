package br.com.rettiwt.activity.login

import android.telecom.ConnectionRequest
import br.com.rettiwt.*
import br.com.rettiwt.models.AuthParams
import br.com.rettiwt.models.AuthResponse
import br.com.rettiwt.models.ConnectResponse
import br.com.rettiwt.models.MethodRequest
import com.google.gson.Gson
import java.security.MessageDigest

class LoginPresenter : LoginContract.Presenter {


    private val socketListener = object : AsyncSocket.SocketListener {
        override fun messageReceived(message: String) {
            message.parse { method, response ->
                val gson = Gson()
                when (method) {
                    METHOD_AUTH -> {
                        val response = gson.fromJson(response, AuthResponse::class.java)
                        PreferencesHelper.putAuthorization(response.data?.authorization)
                        view?.openHome()
                    }
                }
            }
        }

        override fun onError(status: Int?, message: String?) {
            view?.displayMessage(message)
        }
    }

    private var view: LoginContract.View? = null

    override fun onClickEnter(username: String, password: String) {
        if (username.isNotBlank() && password.isNotBlank()) {
            AsyncSocket.send(METHOD_AUTH, AuthParams(username, password.toMd5()))
        }
    }

    override fun attachView(view: LoginContract.View) {
        this.view = view
        AsyncSocket.socketListener = socketListener
    }

    override fun detachView() {
        AsyncSocket.socketListener = null
        view = null
    }
}