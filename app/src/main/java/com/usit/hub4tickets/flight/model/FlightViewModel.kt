package com.usit.hub4tickets.flight.model

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
class FlightViewModel(var context: Context?) {
    var errorMessage: ErrorResponse = FlightViewModel.ErrorResponse(message = "")
    var flightViewModel: FlightViewModel.AirPortDataResponse =
        FlightViewModel.AirPortDataResponse(message = null, responseData = null, status = null)
    var flightListViewModel: FlightViewModel.FlightListResponse =
        FlightViewModel.FlightListResponse(message = null, responseData = null, status = null)
    var multiCityListViewModel: FlightViewModel.MultiCityResponse =
        FlightViewModel.MultiCityResponse(
            maxPrice = null,
            message = null,
            minPrice = null,
            responseData = null,
            status = null
        )

    data class ErrorResponse(
        @Nullable
        @SerializedName("message") var message: String?
    )

    data class MultiCitiesForSearch(
        var date_from: String,
        var fly_from: String,
        var fly_to: String
    )

    data class MultiCityResponse(
        val maxPrice: Double?,
        val message: String?,
        val minPrice: Double?,
        val responseData: List<ResponseDataMulticity>?,
        val status: String?
    ) : Parcelable {
        constructor(source: Parcel) : this(
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readString(),
            source.readValue(Double::class.java.classLoader) as Double?,
            source.createTypedArrayList(ResponseDataMulticity.CREATOR),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeValue(maxPrice)
            writeString(message)
            writeValue(minPrice)
            writeTypedList(responseData)
            writeString(status)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<MultiCityResponse> = object : Parcelable.Creator<MultiCityResponse> {
                override fun createFromParcel(source: Parcel): MultiCityResponse = MultiCityResponse(source)
                override fun newArray(size: Int): Array<MultiCityResponse?> = arrayOfNulls(size)
            }
        }
    }

    data class ResponseDataMulticity(
        val dataProvider: String,
        val deepLink: String,
        val multiCityResults: List<MultiCityResult>,
        val price: Double,
        val totalDuration: String,
        val currencySymbol: String,
        var totalDurationFormatted: String? = ""
    ) : Parcelable {
        constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.createTypedArrayList(MultiCityResult.CREATOR),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(dataProvider)
            writeString(deepLink)
            writeTypedList(multiCityResults)
            writeDouble(price)
            writeString(totalDuration)
            writeString(currencySymbol)
            writeString(totalDurationFormatted)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<ResponseDataMulticity> =
                object : Parcelable.Creator<ResponseDataMulticity> {
                    override fun createFromParcel(source: Parcel): ResponseDataMulticity = ResponseDataMulticity(source)
                    override fun newArray(size: Int): Array<ResponseDataMulticity?> = arrayOfNulls(size)
                }
        }
    }

    data class MultiCityResult(
        val airline: String?,
        val duration: String?,
        val durationInMinutes: Int,
        val endAirPortName: String?,
        val endDate: String?,
        val endTime: String?,
        val flightNo: String?,
        val fromCity: String?,
        val imgUrl: String?,
        val startAirPortName: String?,
        val startDate: String?,
        val startTime: String?,
        val stopCount: Int,
        val stopDetails: ArrayList<StopDetailMulticity>?,
        val toCity: String?
    ) : Parcelable {
        constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.createTypedArrayList(StopDetailMulticity.CREATOR),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(airline)
            writeString(duration)
            writeInt(durationInMinutes)
            writeString(endAirPortName)
            writeString(endDate)
            writeString(endTime)
            writeString(flightNo)
            writeString(fromCity)
            writeString(imgUrl)
            writeString(startAirPortName)
            writeString(startDate)
            writeString(startTime)
            writeInt(stopCount)
            writeTypedList(stopDetails)
            writeString(toCity)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<MultiCityResult> = object : Parcelable.Creator<MultiCityResult> {
                override fun createFromParcel(source: Parcel): MultiCityResult = MultiCityResult(source)
                override fun newArray(size: Int): Array<MultiCityResult?> = arrayOfNulls(size)
            }
        }
    }

    data class StopDetailMulticity(
        val airline: String?,
        val duration: String?,
        val durationInMinutes: Int,
        val endAirPortAddress: String?,
        val endAirPortName: String?,
        val endAirportShortName: String?,
        val endDate: String?,
        val endTime: String?,
        val flightNo: String?,
        val fromCity: String?,
        val imgUrl: String?,
        val startAirPortAddress: String?,
        val startAirPortName: String?,
        val startAirportShortName: String?,
        val startDate: String?,
        val startTime: String?,
        val toCity: String?,
        @Nullable val waitingDuration: String? = ""

    ) : Parcelable {
        constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(airline)
            writeString(duration)
            writeInt(durationInMinutes)
            writeString(endAirPortAddress)
            writeString(endAirPortName)
            writeString(endAirportShortName)
            writeString(endDate)
            writeString(endTime)
            writeString(flightNo)
            writeString(fromCity)
            writeString(imgUrl)
            writeString(startAirPortAddress)
            writeString(startAirPortName)
            writeString(startAirportShortName)
            writeString(startDate)
            writeString(startTime)
            writeString(toCity)
            writeString(waitingDuration)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<StopDetailMulticity> = object : Parcelable.Creator<StopDetailMulticity> {
                override fun createFromParcel(source: Parcel): StopDetailMulticity = StopDetailMulticity(source)
                override fun newArray(size: Int): Array<StopDetailMulticity?> = arrayOfNulls(size)
            }
        }
    }

    data class FlightListResponse(
        @SerializedName("message")
        val message: String? = "",
        @SerializedName("responseData")
        val responseData: List<ResponseData?>?,
        @SerializedName("status")
        val status: String? = "",
        @SerializedName("minPrice")
        val minPrice: String? = "",
        @SerializedName("maxPrice")
        val maxPrice: String? = "",
        @SerializedName("price_from")
        val price_from: String? = "",
        @SerializedName("price_to")
        val price_to: String? = ""
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("currencySymbol")
            val currency: String? = "",
            @SerializedName("dataProvider")
            val dataProvider: String? = "",
            @SerializedName("deepLink")
            val deepLink: String? = "",
            @SerializedName("inbondFlightDetails")
            val inbondFlightDetails: InbondFlightDetails?,
            @SerializedName("outbondFlightDetails")
            val outbondFlightDetails: OutbondFlightDetails?,
            @SerializedName("price")
            val price: String? = "",
            @Nullable
            @SerializedName("totalDuration")
            var totalDuration: String? = "",
            var totalDurationFormatted: String? = ""
        ) : Parcelable {
            data class InbondFlightDetails(
                @SerializedName("airline")
                val airline: String? = "",
                @SerializedName("currency")
                val currency: String? = "",
                @SerializedName("duration")
                val duration: String? = "",
                @SerializedName("endAirPortName")
                val endAirPortName: String? = "",
                @SerializedName("endDate")
                val endDate: String? = "",
                @SerializedName("endTime")
                val endTime: String? = "",
                @SerializedName("flightNo")
                val flightNo: String? = "",
                @SerializedName("fromCity")
                val fromCity: String? = "",
                @SerializedName("imgUrl")
                val imgUrl: String? = "",
                @SerializedName("sortingEndTime")
                val sortingEndTime: String? = "",
                @SerializedName("sortingStartTime")
                val sortingStartTime: String? = "",
                @SerializedName("startAirPortName")
                val startAirPortName: String? = "",
                @SerializedName("startDate")
                val startDate: String? = "",
                @SerializedName("startTime")
                val startTime: String? = "",
                @SerializedName("stopCount")
                val stopCount: Int? = 0,
                @SerializedName("stopDetails")
                val stopDetails: List<StopDetail?>? = listOf(),
                @SerializedName("toCity")
                val toCity: String? = ""
            ) : Parcelable {
                data class StopDetail(
                    @SerializedName("airline")
                    val airline: String? = "",
                    @SerializedName("duration")
                    val duration: String? = "",
                    @SerializedName("endAirPortAddress")
                    val endAirPortAddress: String? = "",
                    @SerializedName("endAirPortName")
                    val endAirPortName: String? = "",
                    @SerializedName("endAirportShortName")
                    val endAirportShortName: String? = "",
                    @SerializedName("endDate")
                    val endDate: String? = "",
                    @SerializedName("endTime")
                    val endTime: String? = "",
                    @SerializedName("flightNo")
                    val flightNo: String? = "",
                    @SerializedName("fromCity")
                    val fromCity: String? = "",
                    @SerializedName("imgUrl")
                    val imgUrl: String? = "",
                    @SerializedName("startAirPortAddress")
                    val startAirPortAddress: String? = "",
                    @SerializedName("startAirPortName")
                    val startAirPortName: String? = "",
                    @SerializedName("startAirportShortName")
                    val startAirportShortName: String? = "",
                    @SerializedName("startDate")
                    val startDate: String? = "",
                    @SerializedName("startTime")
                    val startTime: String? = "",
                    @SerializedName("toCity")
                    val toCity: String? = "",
                    @SerializedName("waitingDuration")
                    val waitingDuration: String? = ""
                ) : Parcelable {
                    companion object {
                        @JvmField
                        val CREATOR: Parcelable.Creator<StopDetail> = object : Parcelable.Creator<StopDetail> {
                            override fun createFromParcel(source: Parcel): StopDetail = StopDetail(source)
                            override fun newArray(size: Int): Array<StopDetail?> = arrayOfNulls(size)
                        }
                    }

                    constructor(source: Parcel) : this(
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString()
                    )

                    override fun describeContents() = 0

                    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                        writeString(airline)
                        writeString(duration)
                        writeString(endAirPortAddress)
                        writeString(endAirPortName)
                        writeString(endAirportShortName)
                        writeString(endDate)
                        writeString(endTime)
                        writeString(flightNo)
                        writeString(fromCity)
                        writeString(imgUrl)
                        writeString(startAirPortAddress)
                        writeString(startAirPortName)
                        writeString(startAirportShortName)
                        writeString(startDate)
                        writeString(startTime)
                        writeString(toCity)
                        writeString(waitingDuration)
                    }
                }

                companion object {
                    @JvmField
                    val CREATOR: Parcelable.Creator<InbondFlightDetails> =
                        object : Parcelable.Creator<InbondFlightDetails> {
                            override fun createFromParcel(source: Parcel): InbondFlightDetails =
                                InbondFlightDetails(source)

                            override fun newArray(size: Int): Array<InbondFlightDetails?> = arrayOfNulls(size)
                        }
                }

                constructor(source: Parcel) : this(
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readValue(Int::class.java.classLoader) as Int?,
                    source.createTypedArrayList(StopDetail.CREATOR),
                    source.readString()
                )

                override fun describeContents() = 0

                override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                    writeString(airline)
                    writeString(currency)
                    writeString(duration)
                    writeString(endAirPortName)
                    writeString(endDate)
                    writeString(endTime)
                    writeString(flightNo)
                    writeString(fromCity)
                    writeString(imgUrl)
                    writeString(sortingEndTime)
                    writeString(sortingStartTime)
                    writeString(startAirPortName)
                    writeString(startDate)
                    writeString(startTime)
                    writeValue(stopCount)
                    writeTypedList(stopDetails)
                    writeString(toCity)
                }
            }

            data class OutbondFlightDetails(
                @SerializedName("airline")
                val airline: String? = "",
                @SerializedName("currency")
                val currency: String? = "",
                @SerializedName("duration")
                val duration: String? = "",
                @SerializedName("endAirPortName")
                val endAirPortName: String? = "",
                @SerializedName("endDate")
                val endDate: String? = "",
                @SerializedName("endTime")
                val endTime: String? = "",
                @SerializedName("flightNo")
                val flightNo: String? = "",
                @SerializedName("fromCity")
                val fromCity: String? = "",
                @SerializedName("imgUrl")
                val imgUrl: String? = "",
                @SerializedName("sortingEndTime")
                val sortingEndTime: String? = "",
                @SerializedName("sortingStartTime")
                val sortingStartTime: String? = "",
                @SerializedName("startAirPortName")
                val startAirPortName: String? = "",
                @SerializedName("startDate")
                val startDate: String? = "",
                @SerializedName("startTime")
                val startTime: String? = "",
                @SerializedName("stopCount")
                val stopCount: Int? = 0,
                @SerializedName("stopDetails")
                val stopDetails: List<StopDetail?>? = listOf(),
                @SerializedName("toCity")
                val toCity: String? = ""
            ) : Parcelable {
                data class StopDetail(
                    @SerializedName("airline")
                    val airline: String? = "",
                    @SerializedName("duration")
                    val duration: String? = "",
                    @SerializedName("endAirPortAddress")
                    val endAirPortAddress: String? = "",
                    @SerializedName("endAirPortName")
                    val endAirPortName: String? = "",
                    @SerializedName("endAirportShortName")
                    val endAirportShortName: String? = "",
                    @SerializedName("endDate")
                    val endDate: String? = "",
                    @SerializedName("endTime")
                    val endTime: String? = "",
                    @SerializedName("flightNo")
                    val flightNo: String? = "",
                    @SerializedName("fromCity")
                    val fromCity: String? = "",
                    @SerializedName("imgUrl")
                    val imgUrl: String? = "",
                    @SerializedName("startAirPortAddress")
                    val startAirPortAddress: String? = "",
                    @SerializedName("startAirPortName")
                    val startAirPortName: String? = "",
                    @SerializedName("startAirportShortName")
                    val startAirportShortName: String? = "",
                    @SerializedName("startDate")
                    val startDate: String? = "",
                    @SerializedName("startTime")
                    val startTime: String? = "",
                    @SerializedName("toCity")
                    val toCity: String? = "",
                    @SerializedName("waitingDuration")
                    val waitingDuration: String? = ""
                ) : Parcelable {
                    companion object {
                        @JvmField
                        val CREATOR: Parcelable.Creator<StopDetail> = object : Parcelable.Creator<StopDetail> {
                            override fun createFromParcel(source: Parcel): StopDetail = StopDetail(source)
                            override fun newArray(size: Int): Array<StopDetail?> = arrayOfNulls(size)
                        }
                    }

                    constructor(source: Parcel) : this(
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString(),
                        source.readString()
                    )

                    override fun describeContents() = 0

                    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                        writeString(airline)
                        writeString(duration)
                        writeString(endAirPortAddress)
                        writeString(endAirPortName)
                        writeString(endAirportShortName)
                        writeString(endDate)
                        writeString(endTime)
                        writeString(flightNo)
                        writeString(fromCity)
                        writeString(imgUrl)
                        writeString(startAirPortAddress)
                        writeString(startAirPortName)
                        writeString(startAirportShortName)
                        writeString(startDate)
                        writeString(startTime)
                        writeString(toCity)
                        writeString(waitingDuration)
                    }
                }

                companion object {
                    @JvmField
                    val CREATOR: Parcelable.Creator<OutbondFlightDetails> =
                        object : Parcelable.Creator<OutbondFlightDetails> {
                            override fun createFromParcel(source: Parcel): OutbondFlightDetails =
                                OutbondFlightDetails(source)

                            override fun newArray(size: Int): Array<OutbondFlightDetails?> = arrayOfNulls(size)
                        }
                }

                constructor(source: Parcel) : this(
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readValue(Int::class.java.classLoader) as Int?,
                    source.createTypedArrayList(StopDetail.CREATOR),
                    source.readString()
                )

                override fun describeContents() = 0

                override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                    writeString(airline)
                    writeString(currency)
                    writeString(duration)
                    writeString(endAirPortName)
                    writeString(endDate)
                    writeString(endTime)
                    writeString(flightNo)
                    writeString(fromCity)
                    writeString(imgUrl)
                    writeString(sortingEndTime)
                    writeString(sortingStartTime)
                    writeString(startAirPortName)
                    writeString(startDate)
                    writeString(startTime)
                    writeValue(stopCount)
                    writeTypedList(stopDetails)
                    writeString(toCity)
                }
            }

            constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readParcelable<InbondFlightDetails>(InbondFlightDetails::class.java.classLoader),
                source.readParcelable<OutbondFlightDetails>(OutbondFlightDetails::class.java.classLoader),
                source.readString(),
                source.readString(),
                source.readString()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(currency)
                writeString(dataProvider)
                writeString(deepLink)
                writeParcelable(inbondFlightDetails, 0)
                writeParcelable(outbondFlightDetails, 0)
                writeString(price)
                writeString(totalDuration)
                writeString(totalDurationFormatted)
            }

            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ResponseData> = object : Parcelable.Creator<ResponseData> {
                    override fun createFromParcel(source: Parcel): ResponseData = ResponseData(source)
                    override fun newArray(size: Int): Array<ResponseData?> = arrayOfNulls(size)
                }
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(ResponseData.CREATOR),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(message)
            writeTypedList(responseData)
            writeString(status)
            writeString(minPrice)
            writeString(maxPrice)
            writeString(price_from)
            writeString(price_to)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<FlightListResponse> = object : Parcelable.Creator<FlightListResponse> {
                override fun createFromParcel(source: Parcel): FlightListResponse = FlightListResponse(source)
                override fun newArray(size: Int): Array<FlightListResponse?> = arrayOfNulls(size)
            }
        }
    }

    data class AirPortDataResponse(
        @Nullable @SerializedName("message") val message: String?,
        @Nullable @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @Nullable @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @Nullable @SerializedName("airPortCode")
            val airPortCode: String?,
            @Nullable @SerializedName("airPortCountry")
            val airPortCountry: String?,
            @Nullable @SerializedName("airPortId")
            val airPortId: Int,
            @Nullable @SerializedName("airPortName")
            val airPortName: String?,
            @Nullable @SerializedName("airPortNameAndCode")
            val airPortNameAndCode: String?
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
                source.readInt(),
                source.readString(),
                source.readString()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(airPortCode)
                writeString(airPortCountry)
                writeInt(airPortId)
                writeString(airPortName)
                writeString(airPortNameAndCode)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<AirPortDataResponse> = object : Parcelable.Creator<AirPortDataResponse> {
                override fun createFromParcel(source: Parcel): AirPortDataResponse = AirPortDataResponse(source)
                override fun newArray(size: Int): Array<AirPortDataResponse?> = arrayOfNulls(size)
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

    data class TripAllDetails(
        val airline: String?,
        val currency: String?,
        val duration: String?,
        val price: String?,
        val endAirPortName: String?,
        val endDate: String?,
        val endTime: String?,
        val flightNo: String?,
        val fromCity: String?,
        val imgUrl: String?,
        val startAirPortName: String?,
        val startDate: String?,
        val startTime: String?,
        val stopCount: String?,
        val stopDetailsInBound: List<FlightListResponse.ResponseData.InbondFlightDetails.StopDetail?>?,
        val stopDetailsOutBound: List<FlightListResponse.ResponseData.OutbondFlightDetails.StopDetail?>?,
        val toCity: String?
    ) : Parcelable {
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<TripAllDetails> = object : Parcelable.Creator<TripAllDetails> {
                override fun createFromParcel(source: Parcel): TripAllDetails = TripAllDetails(source)
                override fun newArray(size: Int): Array<TripAllDetails?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(FlightListResponse.ResponseData.InbondFlightDetails.StopDetail.CREATOR),
            source.createTypedArrayList(FlightListResponse.ResponseData.OutbondFlightDetails.StopDetail.CREATOR),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(airline)
            writeString(currency)
            writeString(duration)
            writeString(price)
            writeString(endAirPortName)
            writeString(endDate)
            writeString(endTime)
            writeString(flightNo)
            writeString(fromCity)
            writeString(imgUrl)
            writeString(startAirPortName)
            writeString(startDate)
            writeString(startTime)
            writeString(stopCount)
            writeTypedList(stopDetailsInBound)
            writeTypedList(stopDetailsOutBound)
            writeString(toCity)
        }
    }

    data class StopDetail(
        @SerializedName("airline")
        val airline: String? = "",
        @SerializedName("duration")
        val duration: String? = "",
        @SerializedName("endAirPortAddress")
        val endAirPortAddress: String? = "",
        @SerializedName("endAirPortName")
        val endAirPortName: String? = "",
        @SerializedName("endAirportShortName")
        val endAirportShortName: String? = "",
        @SerializedName("endDate")
        val endDate: String? = "",
        @SerializedName("endTime")
        val endTime: String? = "",
        @SerializedName("flightNo")
        val flightNo: String? = "",
        @SerializedName("fromCity")
        val fromCity: String? = "",
        @SerializedName("imgUrl")
        val imgUrl: String? = "",
        @SerializedName("startAirPortAddress")
        val startAirPortAddress: String? = "",
        @SerializedName("startAirPortName")
        val startAirPortName: String? = "",
        @SerializedName("startAirportShortName")
        val startAirportShortName: String? = "",
        @SerializedName("startDate")
        val startDate: String? = "",
        @SerializedName("startTime")
        val startTime: String? = "",
        @SerializedName("toCity")
        val toCity: String? = "",
        @SerializedName("waitingDuration")
        val waitingDuration: String? = ""
    ) : Parcelable {
        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<StopDetail> = object : Parcelable.Creator<StopDetail> {
                override fun createFromParcel(source: Parcel): StopDetail = StopDetail(source)
                override fun newArray(size: Int): Array<StopDetail?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(airline)
            writeString(duration)
            writeString(endAirPortAddress)
            writeString(endAirPortName)
            writeString(endAirportShortName)
            writeString(endDate)
            writeString(endTime)
            writeString(flightNo)
            writeString(fromCity)
            writeString(imgUrl)
            writeString(startAirPortAddress)
            writeString(startAirPortName)
            writeString(startAirportShortName)
            writeString(startDate)
            writeString(startTime)
            writeString(toCity)
            writeString(waitingDuration)
        }
    }

}