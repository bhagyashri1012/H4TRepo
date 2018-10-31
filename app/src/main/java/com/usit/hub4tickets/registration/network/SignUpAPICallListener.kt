package com.usit.hub4tickets.domain.api

import com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModel
import com.usit.hub4tickets.utils.Enums

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface SignUpAPICallListener {
    fun onAPICallSucceed(route: Enums.APIRoute, responseModel: SignUpViewModel.SignUpResponse)

    fun onAPICallFailed(route: Enums.APIRoute, throwable: Throwable)
}