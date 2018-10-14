package br.com.rettiwt.activity.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import br.com.rettiwt.R
import br.com.rettiwt.REQUEST_IMAGE
import br.com.rettiwt.activity.login.createLoginIntent
import br.com.rettiwt.models.HomeItemModel
import br.com.rettiwt.setVisible
import br.com.rettiwt.snack
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import android.provider.MediaStore

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private val presenter: HomeContract.Presenter by lazy {
        val presenter = HomePresenter()
        presenter.attachView(this)
        presenter
    }

    private val adapter by lazy {
        val adapter = HomeAdapter(object: HomeAdapter.OnClickListener {
            override fun onClickStar(id: String?) {
                presenter.onClickStar(id)
            }

            override fun onClickRettiwt(id: String?) {
                presenter.onClickRettiwt(id)
            }
        })
        homeRv.adapter = adapter
        homeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        title = "Rettiwt"
        setListeners()
        presenter.startSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                presenter.onClickLogout()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setListeners() {
        homeSendBtn.setOnClickListener {
            presenter.onClickSend(homeEt.text.toString())
            homeEt.setText("")
        }
        homeCameraBtn.setOnClickListener { presenter.onClickCamera() }
    }

    override fun displayNewContentButton(show: Boolean) {
        runOnUiThread {
            homeNewContentBtn.setVisible(show)
        }
    }

    override fun displayItems(items: List<HomeItemModel>) {
        runOnUiThread {
            adapter.list = items
        }
    }

    override fun displayMessage(msg: String?) {
        runOnUiThread {
            applicationContext.toast(msg ?: getString(R.string.standard_error))
        }
    }

    override fun displaySnack(action: () -> Unit) {
        runOnUiThread {
            snack(homeCoordinator, getString(R.string.standard_snack_msg), getString(R.string.standard_snack_btn)) {
                action()
            }
        }
    }

    override fun openGalleryPicker() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE)
    }

    override fun openLogin(invalid: Boolean) {
        startActivity(createLoginIntent())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE) {
            try {
                val uri = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                presenter.onImagePicked(homeEt.text.toString(), bitmap)
                homeEt.setText("")
            } catch (e: Exception) {
                e.printStackTrace()
                displayMessage("Erro ao salvar imagem.")
            }
        }
    }
}

fun Context.createHomeIntent(): Intent {
    val intent = intentFor<HomeActivity>()
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    return intent
}
