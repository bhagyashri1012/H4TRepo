package com.usit.hub4tickets.domain.api.sample

import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModel
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface Service {

    @POST("login")
    fun getLogin(@Body loginDto: Login): Flowable<LoginViewModel.LoginResponse>

    @POST("registration")
    fun getRegistration(@Body signUpDto: SignUp): Flowable<SignUpViewModel.SignUpResponse>

    @POST("SendOTP")
    fun sendOTP(): Flowable<Response>

    @POST("OTPValidator")
    fun getOTPValidator(): Flowable<Response>

    @POST("ResetPassword")
    fun resetPassword(): Flowable<Response>
}