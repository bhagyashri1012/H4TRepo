package com.usit.hub4tickets.dashboard.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Bhagyashri Burade
 * Date: 02/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class DashboardViewModel(var context: Context?) {
    var errorMessage: String? = null
    var dashboradCountriesDomain: DashboardViewModel.CountriesResponse =
        DashboardViewModel.CountriesResponse(message = null, responseData = null, status = null)

    var dashboradLangDomain: DashboardViewModel.LanguageResponse =
        DashboardViewModel.LanguageResponse(message = null, responseData = null, status = null)

    var dashboradCurrencyDomain: DashboardViewModel.CurrencyResponse =
        DashboardViewModel.CurrencyResponse(message = null, currencyData = null, status = null)

    var settingsDomain: DashboardViewModel.SettingsResponse =
        DashboardViewModel.SettingsResponse(responseData = null, message = null, status = null)

    data class SettingsResponse(
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val responseData: ResponseData?,
        @SerializedName("status")
        val status: String?
    ) {
        data class ResponseData(
            @SerializedName("countryId")
            val countryId: Int,
            @SerializedName("countryName")
            val countryName: String,
            @SerializedName("currencyId")
            val currencyId: Int,
            @SerializedName("currencyName")
            val currencyName: String,
            @SerializedName("deviceId")
            val deviceId: String,
            @SerializedName("languageId")
            val languageId: Int,
            @SerializedName("languageName")
            val languageName: String,
            @SerializedName("userDetailsId")
            val userDetailsId: Int,
            @SerializedName("userId")
            val userId: Int
        )
    }

    data class CountriesResponse(
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("countryCode")
            val countryCode: String,
            @SerializedName("countryId")
            val countryId: Int,
            @SerializedName("countryName")
            val countryName: String,
            @SerializedName("msState")
            val msState: List<Any>
        ) : Parcelable {
            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ResponseData> = object : Parcelable.Creator<ResponseData> {
                    override fun createFromParcel(source: Parcel): ResponseData = ResponseData(source)
                    override fun newArray(size: Int): Array<ResponseData?> = arrayOfNulls(size)
                }
            }

            constructor(source: Parcel) : this(
                source.readString(),
                source.readInt(),
                source.readString(),
                ArrayList<Any>().apply { source.readList(this, Any::class.java.classLoader) }
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(countryCode)
                writeInt(countryId)
                writeString(countryName)
                writeList(msState)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<CountriesResponse> = object : Parcelable.Creator<CountriesResponse> {
                override fun createFromParcel(source: Parcel): CountriesResponse = CountriesResponse(source)
                override fun newArray(size: Int): Array<CountriesResponse?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            ArrayList<ResponseData>().apply { source.readList(this, ResponseData::class.java.classLoader) },
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeList(responseData)
            writeString(status)
        }
    }

    data class CurrencyResponse(
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val currencyData: List<CurrencyData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class CurrencyData(
            @SerializedName("code")
            val code: String,
            @SerializedName("currencyId")
            val currencyId: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("symbol")
            val symbol: String
        ) : Parcelable {
            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<CurrencyData> = object : Parcelable.Creator<CurrencyData> {
                    override fun createFromParcel(source: Parcel): CurrencyData = CurrencyData(source)
                    override fun newArray(size: Int): Array<CurrencyData?> = arrayOfNulls(size)
                }
            }

            constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(code)
                writeString(currencyId)
                writeString(name)
                writeString(symbol)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<CurrencyResponse> = object : Parcelable.Creator<CurrencyResponse> {
                override fun createFromParcel(source: Parcel): CurrencyResponse = CurrencyResponse(source)
                override fun newArray(size: Int): Array<CurrencyResponse?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            ArrayList<CurrencyData>().apply { source.readList(this, CurrencyData::class.java.classLoader) },
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeList(currencyData)
            writeString(status)
        }
    }

    data class LanguageResponse(
        @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @SerializedName("message")
        val message: String?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("languageCode")
            val languageCode: String,
            @SerializedName("languageId")
            val languageId: Int,
            @SerializedName("name")
            val name: String
        ) : Parcelable {
            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ResponseData> = object : Parcelable.Creator<ResponseData> {
                    override fun createFromParcel(source: Parcel): ResponseData = ResponseData(source)
                    override fun newArray(size: Int): Array<ResponseData?> = arrayOfNulls(size)
                }
            }

            constructor(source: Parcel) : this(
                source.readString(),
                source.readInt(),
                source.readString()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(languageCode)
                writeInt(languageId)
                writeString(name)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<LanguageResponse> = object : Parcelable.Creator<LanguageResponse> {
                override fun createFromParcel(source: Parcel): LanguageResponse = LanguageResponse(source)
                override fun newArray(size: Int): Array<LanguageResponse?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.createTypedArrayList(ResponseData.CREATOR),
            source.readString(),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeTypedList(responseData)
            writeString(message)
            writeString(status)
        }
    }
}



