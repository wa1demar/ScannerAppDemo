package com.example.zebrascannerappdemo.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateHandler @Inject constructor(@ApplicationContext context: Context) {

    private var _connectivityManager: ConnectivityManager? = null
    private val connectivityManager: ConnectivityManager get() = _connectivityManager!!

    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        _connectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private var networkStatusCallback = mutableListOf<NetworkStatusCallback>()

    fun addNetworkStatusCallback(callback: NetworkStatusCallback) {
        networkStatusCallback.add(callback)
        if (isNetworkActive()) {
            callback.onAvailable()
        } else {
            callback.onLost()
        }
    }

    fun removeNetworkStatusCallback(callback: NetworkStatusCallback) {
        networkStatusCallback.remove(callback)
    }

    fun registerNetworkReceiver() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun unregisterNetworkReceiver() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun isNetworkActive(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN))
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            mainScope.launch {
                networkStatusCallback.forEach { it.onAvailable() }
            }
        }

        override fun onLost(network: Network) {
            mainScope.launch {
                networkStatusCallback.forEach { it.onLost() }
            }
        }
    }

    interface NetworkStatusCallback {
        fun onAvailable()
        fun onLost()
    }
}