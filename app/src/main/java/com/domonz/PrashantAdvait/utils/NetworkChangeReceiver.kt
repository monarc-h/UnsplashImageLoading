package com.domonz.PrashantAdvait.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkChangeReceiver(private val listener: OnNetworkChangeListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            // Network is connected
            listener.onNetworkConnected()
        } else {
            // Network is disconnected
            listener.onNetworkDisconnected()
        }
    }

    interface OnNetworkChangeListener {
        fun onNetworkConnected()
        fun onNetworkDisconnected()
    }
}