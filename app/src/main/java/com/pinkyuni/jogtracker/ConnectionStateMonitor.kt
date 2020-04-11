package com.pinkyuni.jogtracker

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

class ConnectionStateMonitor(context: Context) : LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {

        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                postValue(true)
            }

            override fun onLost(network: Network?) {
                postValue(false)
            }
        }
        return connectivityManagerCallback
    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }
}