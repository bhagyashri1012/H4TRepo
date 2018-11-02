package com.usit.hub4tickets.domain.api.sample

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

data class Response(
     val message: String?
)
data class Login(val deviceId: String, val email: String, val password: String, val deviceType: Int)
data class SignUp(val deviceId: String, var email: String, var password: String, val deviceType: Int)
data class ForgotPassword(var email: String)
data class SentOTP(var email: String, var otp: String)
data class ResetPassword(var email: String, var newpassword: String)

