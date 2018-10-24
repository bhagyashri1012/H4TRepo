package com.usit.hub4tickets.domain.api

import com.usit.hub4tickets.domain.api.sample.LoginResponse
import com.usit.hub4tickets.utils.Enums

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface APICallListener {
    fun onAPICallSucceed(route: Enums.APIRoute, responseModel: LoginResponse)

    fun onAPICallFailed(route: Enums.APIRoute, throwable: Throwable)
}
