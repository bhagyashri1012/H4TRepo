package com.usit.hub4tickets.api.network

import android.util.Log
import com.google.gson.Gson
import com.usit.hub4tickets.domain.api.RootResponseModel
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Bhagyashri Burade
 * Date: 15/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class ErrorResponse() {
    var message: String? = null

    constructor(message: String?) : this() {
        this.message = message
    }

    companion object {
        fun parseError(throwable: Throwable): String? {
            if (throwable is HttpException) {
                return try {
                    val jsonString = throwable.response().errorBody()!!.string()
                    val gson = Gson()
                    val rootResponseModel = gson.fromJson(jsonString, RootResponseModel::class.java)
                    rootResponseModel.message
                } catch (e: IOException) {
                    Log.e("API Error", e.message)
                    ErrorResponse(e.message).message
                }
            }
            return ErrorResponse("Please try again!").message
        }
    }
}
