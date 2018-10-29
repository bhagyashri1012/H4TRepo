package com.usit.hub4tickets.domain.api.sample

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
    fun getLogin(@Body loginDto: Login): Flowable<LoginResponse>

    @POST("registration")
    fun getRegistration(): Flowable<Response>

    @POST("SendOTP")
    fun sendOTP(): Flowable<Response>

    @POST("OTPValidator")
    fun getOTPValidator(): Flowable<Response>

    @POST("ResetPassword")
    fun resetPassword(): Flowable<Response>
}