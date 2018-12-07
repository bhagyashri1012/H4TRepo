package com.usit.hub4tickets

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.support.multidex.MultiDex



/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
open class MainApplication : Application() {

    var connected: Boolean = false

    companion object {
        private var instance: MainApplication? = null

        /**
         * singleton class instance
         */
        val getInstance: MainApplication
            get() {
                if (instance == null) {
                    synchronized(MainApplication::class.java) {
                        if (instance == null) {
                            instance = MainApplication()
                        }
                    }
                }
                return instance!!
            }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    fun isConnected(): Boolean {
        val cm = applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        connected = activeNetwork != null && activeNetwork.isConnected
        return connected
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this@MainApplication)
    }
}