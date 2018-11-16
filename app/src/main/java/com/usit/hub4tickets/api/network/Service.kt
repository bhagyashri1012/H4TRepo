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

    @POST("registeruser")
    fun getRegistration(@Body signUpDto: SignUp): Flowable<SignUpViewModel.SignUpResponse>

    @POST("otp")
    fun sendOtp(@Body signUpDto: SendOtp): Flowable<LoginViewModel.LoginResponse>

    @POST("otp/verify")
    fun verifyOTP(@Body signUpDto: VerifyOTP): Flowable<LoginViewModel.LoginResponse>

    @POST("resetpassword")
    fun resetPassword(@Body resetPasswordDto: ResetPassword): Flowable<LoginViewModel.LoginResponse>

    @GET("countries")
    fun getCountries(): Flowable<DashboardViewModel.CountriesResponse>

    @GET("langauges")
    fun getLangauges(): Flowable<DashboardViewModel.LanguageResponse>

    @GET("currencies")
    fun getCurrencies(): Flowable<DashboardViewModel.CurrencyResponse>

    @POST("states")
    fun getStates(): Flowable<DashboardViewModel.CountriesResponse>

    @POST("cities")
    fun getCities(): Flowable<DashboardViewModel.CountriesResponse>

    @POST("settingdata")
    fun settingsData(@Body settingsData: SettingsData): Flowable<DashboardViewModel.CountriesResponse>

    @POST("savesettingdata")
    fun saveSettingsData(@Body settingsData: SaveSettingsData): Flowable<DashboardViewModel.CountriesResponse>

    @POST("changepassword")
    fun changePassword(@Body changePasswordDto: ChangePassword): Flowable<DashboardViewModel.CountriesResponse>

    @POST("getprofile")
    fun getProfileData(@Body profileData: ProfileData): Flowable<DashboardViewModel.CountriesResponse>

    @POST("updateprofile")
    fun updateProfileData(@Body updateProfileData: UpdateProfileData): Flowable<DashboardViewModel.CountriesResponse>

}