package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import com.google.gson.annotations.SerializedName


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class LoginViewModel(var context: Context?) {
    var errorMessage: String? = null

    var loginDomain: LoginResponse =
        LoginResponse(responseData = null, message = null, status = null, timeStamp = null)

    data class LoginResponse(
        @SerializedName("responseData")
        val responseData: ResponseData?,
        @SerializedName("message")
        var message: String?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("timeStamp")
        val timeStamp: String?
    )

    data class ResponseData(
        val city: String,
        val country: String,
        val deviceId: String,
        val dno: Any,
        val email: String,
        val firstname: String,
        val homeairport: String,
        val language: String,
        val lastname: String,
        val otp: String,
        val otpFlag: Int,
        val password: String,
        val phonenumber: String,
        val state: String,
        val timezone: String,
        val timezoneid: Any,
        val userId: Int
    )


}