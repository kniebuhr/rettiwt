package br.com.rettiwt

import android.os.Handler
import android.util.Log
import br.com.rettiwt.models.ConnectResponse
import br.com.rettiwt.models.KeepAliveParams
import br.com.rettiwt.models.MethodRequest
import br.com.rettiwt.models.MethodResponse
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread

object AsyncSocket {

    interface SocketListener {
        fun messageReceived(message: String)
        fun onError(status: Int?, message: String?)
    }

    private var socket: Socket? = null
    private var socketOutput: BufferedReader? = null
    var socketListener: SocketListener? = null

    fun start() {
        thread {
            try {
                socket = Socket("192.168.15.50", 5000)
                socketOutput = BufferedReader(InputStreamReader(socket?.getInputStream()))
                keepAlive()
                listen()
            } catch (e: Exception) {
                parseException(e)
            }
        }
    }

    private var handler: Handler? = Handler()
    private var runnable: Runnable? = Runnable {
        send(METHOD_KEEP_ALIVE, KeepAliveParams(PreferencesHelper.getSocketId()))
        keepAlive()
    }

    private fun keepAlive() {
        handler?.postDelayed(runnable, 12500)
    }

    private fun removeKeepAlive() {
        runnable?.let { handler?.removeCallbacks(it) }
    }

    private fun finish() {
        socket = null
        socketOutput = null
    }

    private fun listen() {
        while (true) {
            val line = socketOutput?.readLine()
            if (line == null) {
                break
            } else if (line.isNotBlank()) {
                Log.e("SOCKET_TAG", line)
                val gson = Gson()
                val response = gson.fromJson(line, MethodResponse::class.java)
                if (response.status != 1200) {
                    socketListener?.onError(response.status, response.message)
                } else if (response.method == METHOD_CONNECT) {
                    val response = gson.fromJson(line, ConnectResponse::class.java)
                    PreferencesHelper.putSocketId(response.data?.socket_id)
                } else {
                    socketListener?.messageReceived(line)
                }
            }
        }
    }

    fun send(method: String?, params: Any? = null) {
        socket?.let {
            try {
                val gson = Gson()
                val json = gson.toJson(MethodRequest(method, APP_ID, PreferencesHelper.getAuthorization(), params))
                thread { it.getOutputStream().write(json.toByteArray()) }
            } catch (e: Exception) {
                parseException(e)
            }
        } ?: run {
            socketListener?.onError(1702, "Tente novamente")
            start()
        }
    }

    private fun parseException(e: Exception) {
        e.printStackTrace()
        when (e) {
            is ConnectException -> socketListener?.onError(1701, "Erro de conexão")
            is SocketException -> {
                removeKeepAlive()
                finish()
                socketListener?.onError(1701, "Erro de conexão")
            }
            else -> socketListener?.onError(1404, "Erro desconhecido")
        }
    }

}