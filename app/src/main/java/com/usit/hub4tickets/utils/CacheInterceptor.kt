package com.usit.hub4tickets.utils


import com.usit.hub4tickets.MainApplication
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class CacheInterceptor : Interceptor {

    var body = ""
        private set

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.method() == "GET") {
            if (MainApplication.getInstance.isConnected()) {
                request = request.newBuilder().removeHeader("pragma").removeHeader("Cache-Control")
                    .header("Cache-Control", "only-if-cached").build()
            } else {
                request = request.newBuilder().removeHeader("pragma").removeHeader("Cache-Control")
                    .header("Cache-control", "max-stale=2419200").build()
            }
        }
        val response = chain.proceed(request)
        body = response.body()!!.string()
        return response.newBuilder().body(ResponseBody.create(response.body()!!.contentType(), body)).build()
    }
}
