package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SignUpViewModel(var context: Context?) {
    var errorMessage: String? = null
    var signUpDomain: SignUpResponse =
        SignUpResponse(message = null, responseData = null, status = null)

    data class SignUpResponse(
        val message: String?,
        val responseData: LoginViewModel.ResponseData?,
        val status: String?
    )

}