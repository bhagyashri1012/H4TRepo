package com.usit.hub4tickets.dashboard.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.Nullable

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

    var dashboradStateDomain: DashboardViewModel.StateResponse =
        DashboardViewModel.StateResponse(message = null, responseData = null, status = null)

    var dashboradCityDomain: DashboardViewModel.CityResponse =
        DashboardViewModel.CityResponse(message = null, responseData = null, status = null)

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


    data class CurrencyResponse(
        @Nullable
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val currencyData: List<CurrencyData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class CurrencyData(
            @Nullable
            @SerializedName("countryCode")
            val countryCode: String?,
            @Nullable
            @SerializedName("currencyCode")
            val currencyCode: String?,
            @Nullable
            @SerializedName("currencyName")
            val currencyName: String?
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
                source.readString()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(countryCode)
                writeString(currencyCode)
                writeString(currencyName)
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
            source.createTypedArrayList(CurrencyData.CREATOR),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeTypedList(currencyData)
            writeString(status)
        }
    }

    data class LanguageResponse(
        @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @Nullable @SerializedName("message") val message: String?,
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

    data class CityResponse(
        @Nullable @SerializedName("message") val message: String?,
        @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("cityId")
            val cityId: String,
            @SerializedName("cityname")
            val cityname: String,
            @SerializedName("stateId")
            val stateId: String
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
                source.readString(),
                source.readString()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(cityId)
                writeString(cityname)
                writeString(stateId)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<CityResponse> = object : Parcelable.Creator<CityResponse> {
                override fun createFromParcel(source: Parcel): CityResponse = CityResponse(source)
                override fun newArray(size: Int): Array<CityResponse?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(ResponseData.CREATOR),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeTypedList(responseData)
            writeString(status)
        }
    }

    data class StateResponse(
        @Nullable @SerializedName("message") val message: String?,
        @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("countryId")
            val countryId: CountryId,
            @SerializedName("stateId")
            val stateId: String,
            @SerializedName("stateName")
            val stateName: String
        ) : Parcelable {
            data class CountryId(
                @SerializedName("countryCode")
                val countryCode: String,
                @SerializedName("countryId")
                val countryId: String,
                @SerializedName("countryName")
                val countryName: String,
                @SerializedName("msState")
                val msState: List<Any>
            ) : Parcelable {
                companion object {
                    @JvmField
                    val CREATOR: Parcelable.Creator<CountryId> = object : Parcelable.Creator<CountryId> {
                        override fun createFromParcel(source: Parcel): CountryId = CountryId(source)
                        override fun newArray(size: Int): Array<CountryId?> = arrayOfNulls(size)
                    }
                }

                constructor(source: Parcel) : this(
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    ArrayList<Any>().apply { source.readList(this, Any::class.java.classLoader) }
                )

                override fun describeContents() = 0

                override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                    writeString(countryCode)
                    writeString(countryId)
                    writeString(countryName)
                    writeList(msState)
                }
            }

            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ResponseData> = object : Parcelable.Creator<ResponseData> {
                    override fun createFromParcel(source: Parcel): ResponseData = ResponseData(source)
                    override fun newArray(size: Int): Array<ResponseData?> = arrayOfNulls(size)
                }
            }

            constructor(source: Parcel) : this(
                source.readParcelable<CountryId>(CountryId::class.java.classLoader),
                source.readString(),
                source.readString()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeParcelable(countryId, 0)
                writeString(stateId)
                writeString(stateName)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<StateResponse> = object : Parcelable.Creator<StateResponse> {
                override fun createFromParcel(source: Parcel): StateResponse = StateResponse(source)
                override fun newArray(size: Int): Array<StateResponse?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(ResponseData.CREATOR),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeTypedList(responseData)
            writeString(status)
        }
    }

    data class CountriesResponse(
        @Nullable @SerializedName("message") val message: String?,
        @SerializedName("responseData")
        val responseData: List<CountriesResponseData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class CountriesResponseData(
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
                val CREATOR: Parcelable.Creator<CountriesResponseData> =
                    object : Parcelable.Creator<CountriesResponseData> {
                        override fun createFromParcel(source: Parcel): CountriesResponseData =
                            CountriesResponseData(source)

                        override fun newArray(size: Int): Array<CountriesResponseData?> = arrayOfNulls(size)
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
            source.createTypedArrayList(CountriesResponseData.CREATOR),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeTypedList(responseData)
            writeString(status)
        }
    }


}



