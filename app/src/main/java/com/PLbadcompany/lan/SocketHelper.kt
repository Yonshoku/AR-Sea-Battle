package com.PLbadcompany.lan

import android.util.Log
import java.net.ServerSocket

class SocketHelper {
    private val TAG : String = this::class.java.simpleName
    lateinit var serverSocket : ServerSocket
    var localPort : Int = -1

    // Init socket and get free port
    fun initializeSocket() {
        // Random unused port will be selected automatically
        serverSocket = ServerSocket(0).also { socket ->
            localPort = socket.localPort
        }

        Log.v(TAG, "Socket has been initialized with port: $localPort")
    }

    // Stop lan
    fun closeSocket() {
        serverSocket.close()

        Log.v(TAG, "Socket has been closed")
    }

    fun acceptSocket() {
        serverSocket.accept()

        Log.v(TAG, "Socket has been accepted")
    }
}