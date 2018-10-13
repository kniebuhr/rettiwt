package br.com.rettiwt

import android.os.Handler
import br.com.rettiwt.models.ConnectResponse
import br.com.rettiwt.models.KeepAliveParams
import br.com.rettiwt.models.MethodRequest
import br.com.rettiwt.models.MethodResponse
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.Socket
import kotlin.concurrent.thread

object AsyncSocket {

    interface SocketListener {
        fun messageReceived(message: String)
        fun onError(message: String?)
    }

    private var socket: Socket? = null
    private var socketOutput: BufferedReader? = null
    var socketListener: SocketListener? = null

    fun start() {
        thread {
            try {
                socket = Socket("192.168.100.103", 5000)
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
        send(MethodRequest(METHOD_KEEP_ALIVE, APP_ID, PreferencesHelper.getAuthorization(), KeepAliveParams(PreferencesHelper.getSocketId())))
        keepAlive()
    }

    private fun keepAlive() {
        handler?.postDelayed(runnable, 250000)
    }

    private fun listen() {
        while (true) {
            val line = socketOutput?.readLine()
            if (line == null) {
                break
            } else if (line.isNotBlank()) {
                val gson = Gson()
                val response = gson.fromJson(line, MethodResponse::class.java)
                if (response.status !in listOf(200, 1200)) {
                    socketListener?.onError(response.message)
                } else if (response.method == METHOD_CONNECT) {
                    val response = gson.fromJson(line, ConnectResponse::class.java)
                    PreferencesHelper.putSocketId(response.data?.socket_id)
                } else {
                    socketListener?.messageReceived(line)
                }
            }
        }
    }

    fun send(data: Any) {
        socket?.let {
            try {
                val gson = Gson()
                val json = gson.toJson(data)
                thread { it.getOutputStream().write(json.toByteArray()) }
            } catch (e: Exception) {
                parseException(e)
            }
        } ?: run {
            socketListener?.onError("Socket foi fechado, tente novamente")
            start()
        }
    }

    private fun parseException(e: Exception) {
        e.printStackTrace()
        when (e) {
            is ConnectException -> {
                socketListener?.onError("Erro de conexão")
            }
            else -> {
                socketListener?.onError("Erro desconhecido")
            }
        }
    }


}