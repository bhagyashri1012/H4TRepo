package com.usit.hub4tickets.domain.api

import android.support.v7.app.AlertDialog
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.utils.Enums

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface LoginAPICallListener {
    fun onAPICallSucceed(route: Enums.APIRoute, responseModel: LoginViewModel.LoginResponse)
    fun onVerifyOtpAPICallSucceed(
        route: Enums.APIRoute,
        response: LoginViewModel.LoginResponse,
        dialogBuilder: AlertDialog
    )
    fun onSentOtpAPICallSucceed(route: Enums.APIRoute, response: LoginViewModel.LoginResponse)
    fun onForgotPasswordAPICallSucceed(
        route: Enums.APIRoute,
        response: LoginViewModel.LoginResponse,
        dialogBuilder: AlertDialog
    )
    fun onAPICallFailed(route: Enums.APIRoute, message: String?)
}
