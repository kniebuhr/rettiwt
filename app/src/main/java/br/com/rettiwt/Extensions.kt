package br.com.rettiwt

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import br.com.rettiwt.models.MethodResponse
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

fun String.changeDateFormat(old: String, new: String): String {
    val formatOldToDate = SimpleDateFormat(old, Locale("pt", "BR"))
    val formatDateToNew = SimpleDateFormat(new, Locale("pt", "BR"))
    val date = formatOldToDate.parse(this)
    return formatDateToNew.format(date)
}

fun snack(coordinatorLayout: CoordinatorLayout, msg: String, button: String, action: (view: View) -> Unit) {
    Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE).setAction(button) {
        action(it)
    }.show()
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun String.parse(action: (method: String?, message: String) -> Unit) {
    val gson = Gson()
    val method = gson.fromJson(this, MethodResponse::class.java)
    action(allMethods.find { it == method.method }, this)
}
