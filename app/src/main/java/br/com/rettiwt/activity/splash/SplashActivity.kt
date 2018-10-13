package br.com.rettiwt.activity.splash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import br.com.rettiwt.AsyncSocket
import br.com.rettiwt.PreferencesHelper
import br.com.rettiwt.activity.home.createHomeIntent
import br.com.rettiwt.activity.login.createLoginIntent

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferencesHelper.start(this)
        AsyncSocket.start()
        Handler().postDelayed({
            if (PreferencesHelper.getAuthorization().isNullOrBlank()) {
                startActivity(createLoginIntent())
            } else {
                startActivity(createHomeIntent())
            }
            finish()
        }, 2000)
    }
}
