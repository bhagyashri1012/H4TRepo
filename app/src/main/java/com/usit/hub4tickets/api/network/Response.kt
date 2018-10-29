package com.usit.hub4tickets.domain.api.sample

import com.usit.hub4tickets.domain.api.RootResponseModel

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

data class Response(
    val message: String?
) : RootResponseModel() {

    constructor() : this(message = null)
}

data class Login(val device_id: String, val email: String, val password: String, val deviceFlag: Int)
data class LoginResponse(val status: String, val message: String)