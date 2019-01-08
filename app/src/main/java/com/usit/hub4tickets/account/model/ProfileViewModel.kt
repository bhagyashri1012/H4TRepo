package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
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
    var settingsDomain: SettingsResponse =
        SettingsResponse(responseData = null, message = null, status = null)

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
                val dname: String?,
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
            val countryName: String?,
            @Nullable
            @SerializedName("currencyName")
            val currencyName: String?,
            @Nullable
            @SerializedName("deviceId")
            val deviceId: String?,
            @Nullable
            @SerializedName("languageId")
            val languageId: Int,
            @Nullable
            @SerializedName("languageName")
            val languageName: String?,
            @Nullable
            @SerializedName("latestCurrencyId")
            val latestCurrencyId: String?,
            @Nullable
            @SerializedName("userDetailsId")
            val userDetailsId: Int,
            @Nullable
            @SerializedName("userId")
            val userId: Int
        )
    }

    data class SettingsResponse(
        @Nullable
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val responseData: ResponseData?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("countryId")
            val countryId: Int,
            @SerializedName("countryName")
            val countryName: String,
            @SerializedName("currencyName")
            val currencyName: String,
            @SerializedName("deviceId")
            val deviceId: String,
            @SerializedName("languageId")
            val languageId: Int,
            @SerializedName("languageName")
            val languageName: String,
            @SerializedName("latestCurrencyId")
            val latestCurrencyId: String,
            @SerializedName("userDetailsId")
            val userDetailsId: Int
        ) : Parcelable {
            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ResponseData> = object : Parcelable.Creator<ResponseData> {
                    override fun createFromParcel(source: Parcel): ResponseData = ResponseData(source)
                    override fun newArray(size: Int): Array<ResponseData?> = arrayOfNulls(size)
                }
            }

            constructor(source: Parcel) : this(
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readInt(),
                source.readString(),
                source.readString(),
                source.readInt()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(countryId)
                writeString(countryName)
                writeString(currencyName)
                writeString(deviceId)
                writeInt(languageId)
                writeString(languageName)
                writeString(latestCurrencyId)
                writeInt(userDetailsId)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SettingsResponse> = object : Parcelable.Creator<SettingsResponse> {
                override fun createFromParcel(source: Parcel): SettingsResponse = SettingsResponse(source)
                override fun newArray(size: Int): Array<SettingsResponse?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            source.readParcelable<ResponseData>(ResponseData::class.java.classLoader),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeParcelable(responseData, 0)
            writeString(status)
        }
    }
}