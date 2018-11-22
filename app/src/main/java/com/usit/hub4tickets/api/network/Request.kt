package com.usit.hub4tickets.domain.api.sample

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

data class Login(
    val deviceId: String,
    val email: String,
    val password: String,
    val deviceType: Int
)

data class SignUp(
    val deviceId: String,
    var email: String,
    var password: String,
    val deviceType: Int
)

data class SendOtp(
    val deviceId: String,
    var email: String
)

data class VerifyOTP(
    val deviceId: String,
    var email: String,
    var otp: String
)

data class ResetPassword(
    val deviceId: String,
    var email: String,
    var password: String
)

data class SettingsData(
    val deviceId: String,
    var userId: String
)

data class SaveSettingsData(
    val deviceId: String,
    var userId: String,
    var countryId: String,
    var currencyId: String,
    var languageId: String
)

data class ChangePassword(
    val deviceId: String,
    var userId: String,
    var password: String,
    var newPassword: String
)

data class ProfileData(
    val deviceId: String,
    var userId: String
)

data class UpdateProfileData(
    var userId: String,
    val deviceId: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var homeAirport: String,
    var timezoneId: String,
    var countryId: String,
    var stateId: String,
    var cityId: String,
    var languageId: String
)

