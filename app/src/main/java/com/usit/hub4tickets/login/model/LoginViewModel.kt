package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class LoginViewModel(var context: Context?) {
    var errorMessage: String? = null
    var loginDomain: LoginResponse = LoginResponse(message = null, responseData = null, status = null)

    data class LoginResponse(
        val message: String?,
        val responseData: ResponseData?,
        val status: String?
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