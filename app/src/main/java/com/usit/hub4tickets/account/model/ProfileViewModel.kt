package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import com.google.gson.annotations.SerializedName


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class ProfileViewModel(var context: Context?) {
    var errorMessage: String? = null
    var profileDomain: ProfileResponse =
        ProfileResponse(message = null, responseData = null, status = null)

    data class ProfileResponse(
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val responseData: ResponseData?,
        @SerializedName("status")
        val status: String?
    ) {
        data class ResponseData(
            @SerializedName("city")
            val city: String,
            @SerializedName("country")
            val country: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("firstName")
            val firstName: String,
            @SerializedName("homeAirPort")
            val homeAirPort: String,
            @SerializedName("language")
            val language: String,
            @SerializedName("lastName")
            val lastName: String,
            @SerializedName("phoneNumber")
            val phoneNumber: String,
            @SerializedName("state")
            val state: String,
            @SerializedName("timeZone")
            val timeZone: String,
            @SerializedName("dNo")
            val dNo: DNo,
            @SerializedName("deviceId")
            val deviceId: String,
            @SerializedName("userId")
            val userId: Int
        ) {
            data class DNo(
                @SerializedName("dname")
                val dname: String,
                @SerializedName("dno")
                val dno: Int
            )
        }
    }
}