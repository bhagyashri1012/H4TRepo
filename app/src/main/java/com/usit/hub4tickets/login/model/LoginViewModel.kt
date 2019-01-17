package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.Nullable


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class LoginViewModel(var context: Context?) {
    var errorMessage: String? = ""

    var loginDomain: LoginResponse =
        LoginResponse(responseData = null, message = null, status = null, timeStamp = null)

    data class LoginResponse(
        @Nullable
        @SerializedName("responseData")
        val responseData: ResponseData?,
        @Nullable
        @SerializedName("message")
        var message: String?,
        @Nullable
        @SerializedName("status")
        val status: String?,
        @Nullable
        @SerializedName("timeStamp")
        val timeStamp: String?
    )

    data class ResponseData(
        val country: String,
        val deviceId: String,
        val dno: Any,
        val email: String,
        val firstname: String,
        val homeairport: String,
        val lastname: String,
        val otp: String,
        val otpFlag: Int,
        val password: String,
        val phonenumber: String,
        val state: String,
        val userId: Int
    )


}