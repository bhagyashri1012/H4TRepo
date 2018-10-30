package com.usit.hub4tickets.domain.api

import com.usit.hub4tickets.BuildConfig
import com.usit.hub4tickets.domain.api.sample.*
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModel
import com.usit.hub4tickets.utils.Constant
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class APICallManager {
    private var endPoint = Constant.Path.DEFAULT_URL_API
    lateinit var authorizationKey: String

    var apiManager: APIManager

    init {
        if (BuildConfig.DEBUG)
            endPoint = Constant.Path.DEFAULT_URL_API

        // enable logging
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(endPoint)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // registering API endpoint manager
        this.apiManager = APIManager()
    }

    companion object {
        private var instance: APICallManager? = null
        private lateinit var retrofit: Retrofit

        /**
         * singleton class instance
         */
        val getInstance: APICallManager
            get() {
                if (instance == null) {
                    synchronized(APICallManager::class.java) {
                        if (instance == null) {
                            instance = APICallManager()
                        }
                    }
                }
                return instance!!
            }

        fun <T> getService(serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }
    }

    // region Service Managers
    /**
     * A sample API manager.
     * TODO: Replace this with your own API manager class
     */
    inner class APIManager {
        private val service by lazy {
            getService(Service::class.java)
        }

        fun getLogin(device_id: String, email: String, password: String, deviceFlag: Int): Flowable<LoginViewModel.LoginResponse> {
            val login = Login(device_id, email, password, deviceFlag)
            return service.getLogin(login)
        }

        fun getSignUp(device_id: String, email: String, password: String, deviceFlag: Int): Flowable<SignUpViewModel.SignUpResponse> {
            val signUp = SignUp(device_id, email, password, deviceFlag)
            return service.getRegistration(signUp)
        }
    }
    //endregion
}
