package br.com.rettiwt

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {

    private const val SHARED_PREFERENCES = "SHARED_PREFERENCES"

    private const val AUTHORIZATION = "AUTHORIZATION"
    private const val SOCKETID = "SOCKETID"
    private lateinit var sharedPreferences: SharedPreferences

    fun start(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun putAuthorization(auth: String?) {
        sharedPreferences.edit().putString(AUTHORIZATION, auth).apply()
    }

    fun getAuthorization(): String? {
        return sharedPreferences.getString(AUTHORIZATION, null)
    }

    fun putSocketId(socketId: String?) {
        sharedPreferences.edit().putString(SOCKETID, socketId).apply()
    }

    fun getSocketId(): String? {
        return sharedPreferences.getString(SOCKETID, null)
    }
}