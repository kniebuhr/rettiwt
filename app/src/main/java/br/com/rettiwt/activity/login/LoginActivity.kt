package br.com.rettiwt.activity.login

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.rettiwt.R
import br.com.rettiwt.activity.home.createHomeIntent
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private val presenter: LoginContract.Presenter by lazy {
        val presenter = LoginPresenter()
        presenter.attachView(this)
        presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setListeners()
    }

    private fun setListeners() {
        loginBtn.setOnClickListener {
            presenter.onClickEnter(loginUserEt.text.toString(), loginPasswordEt.text.toString())
        }
    }

    override fun displayMessage(msg: String?) {
        runOnUiThread {
            toast(msg ?: getString(R.string.standard_error))
        }
    }

    override fun openHome() {
        startActivity(createHomeIntent())
    }
}

fun Context.createLoginIntent(): Intent {
    val intent = intentFor<LoginActivity>()
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    return intent
}
