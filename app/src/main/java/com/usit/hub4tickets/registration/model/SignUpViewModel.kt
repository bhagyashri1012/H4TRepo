package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import io.reactivex.annotations.Nullable

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SignUpViewModel(var context: Context?) {
    var errorMessage: String = ""
    var signUpDomain: SignUpResponse =
        SignUpResponse(message = "", responseData = null, status = null)

    data class SignUpResponse(
        @Nullable
        val message: String,
        val responseData: ResponseData?,
        val status: String?
    )

    data class ResponseData(
        val userId: Int,
        val details: String,
        val message: String,
        val timeStamp: Long
    )
}