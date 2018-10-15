package br.com.rettiwt.activity.config

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import br.com.rettiwt.AsyncSocket
import br.com.rettiwt.R
import kotlinx.android.synthetic.main.activity_config.*
import org.jetbrains.anko.intentFor

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        title = getString(R.string.config_title)
        setListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListeners() {
        configBtn.setOnClickListener {
            AsyncSocket.updateSocketIpv4(configEt.text.toString())
            finish()
        }
    }
}

fun Context.createConfigIntent() = intentFor<ConfigActivity>()
