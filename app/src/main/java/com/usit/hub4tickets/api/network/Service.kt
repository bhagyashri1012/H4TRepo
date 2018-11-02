package com.usit.hub4tickets.domain.api.sample

import com.usit.hub4tickets.dashboard.model.DashboardViewModel
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModel
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface Service {

    @POST("login")
    fun getLogin(@Body loginDto: Login): Flowable<LoginViewModel.LoginResponse>

    @POST("users")
    fun getRegistration(@Body signUpDto: SignUp): Flowable<SignUpViewModel.SignUpResponse>

    @POST("otp")
    fun forgotPassword(@Body signUpDto: ForgotPassword): Flowable<LoginViewModel.LoginResponse>

    @POST("otp/verify")
    fun verifyOTP(@Body signUpDto: SentOTP): Flowable<LoginViewModel.LoginResponse>

    @POST("resetpassword")
    fun resetPassword(@Body resetPasswordDto: ResetPassword): Flowable<LoginViewModel.LoginResponse>

    @GET("countries")
    fun getCountries(): Flowable<DashboardViewModel.CountriesResponse>

    @GET("langauges")
    fun getLangauges(): Flowable<DashboardViewModel.LanguageResponse>

    @GET("currencies")
    fun getCurrencies(): Flowable<DashboardViewModel.CountriesResponse>

    @GET("states")
    fun getStates(): Flowable<DashboardViewModel.CountriesResponse>

    @GET("cities")
    fun getCities(): Flowable<DashboardViewModel.CountriesResponse>

}