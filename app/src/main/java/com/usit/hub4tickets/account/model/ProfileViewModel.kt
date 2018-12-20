package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.Nullable


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
        @Nullable
        @SerializedName("message") val message: String?,
        @Nullable @SerializedName("responseData")
        val responseData: ResponseData?,
        @Nullable @SerializedName("status")
        val status: String?
    ) {
        data class ResponseData(
            @Nullable @SerializedName("city")
            val city: String?,
            @Nullable @SerializedName("country")
            val country: String?,
            @Nullable @SerializedName("currency")
            val currency: String?,
            @Nullable @SerializedName("email")
            val email: String?,
            @Nullable @SerializedName("firstName")
            val firstName: String?,
            @Nullable @SerializedName("homeAirPort")
            val homeAirPort: String?,
            @Nullable @SerializedName("language")
            val language: String?,
            @Nullable @SerializedName("lastName")
            val lastName: String?,
            @Nullable @SerializedName("phoneNumber")
            val phoneNumber: String?,
            @Nullable @SerializedName("state")
            val state: String?,
            @Nullable @SerializedName("timeZone")
            val timeZone: String?,
            @Nullable @SerializedName("dNo")
            val dNo: DNo?,
            @Nullable @SerializedName("deviceId")
            val deviceId: String?,
            @Nullable @SerializedName("countryId")
            val countryId: String?,
            @Nullable @SerializedName("stateId")
            val stateId: String?,
            @Nullable @SerializedName("cityId")
            val cityId: String?,
            @Nullable @SerializedName("userId")
            val userId: Int?,
            @Nullable
            @SerializedName("promoChecked")
            val promoChecked: Int?,
            @Nullable
            @SerializedName("sqUserdetails")
            val sqUserdetails: SqUserdetails?
        ) {
            data class DNo(
                @Nullable @SerializedName("dname")
                val dname: String,
                @Nullable @SerializedName("dno")
                val dno: Int
            )
        }

        data class SqUserdetails(
            @Nullable
            @SerializedName("countryId")
            val countryId: Int,
            @Nullable
            @SerializedName("countryName")
            val countryName: String,
            @Nullable
            @SerializedName("currencyName")
            val currencyName: String,
            @Nullable
            @SerializedName("deviceId")
            val deviceId: String,
            @Nullable
            @SerializedName("languageId")
            val languageId: Int,
            @Nullable
            @SerializedName("languageName")
            val languageName: String,
            @Nullable
            @SerializedName("latestCurrencyId")
            val latestCurrencyId: String,
            @Nullable
            @SerializedName("userDetailsId")
            val userDetailsId: Int,
            @Nullable
            @SerializedName("userId")
            val userId: Int
        )
    }
}