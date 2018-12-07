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

    data class ErrorResponse(
        @Nullable
@SerializedName("message") var message: String
    )

    data class FlightListResponse(
        @Nullable
@SerializedName("message")
        val message: String?,
        @Nullable  @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @Nullable  @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @Nullable  @SerializedName("currency")
            val currency: String,
            @Nullable  @SerializedName("dataProvider")
            val dataProvider: String,

            @Nullable
              @SerializedName("inbondFlightDetails")
            val inbondFlightDetails: InbondFlightDetails?,
            @Nullable  @SerializedName("outbondFlightDetails")
            val outbondFlightDetails: OutbondFlightDetails?,
            @Nullable  @SerializedName("price")
            val price: Double
        ) : Parcelable {
            data class InbondFlightDetails(
                @Nullable  @SerializedName("airline")
                val airline: String,
                @Nullable  @SerializedName("currency")
                val currency: String,
                @Nullable  @SerializedName("duration")
                val duration: String,
                @Nullable  @SerializedName("endAirPortName")
                val endAirPortName: String,
                @Nullable  @SerializedName("endDate")
                val endDate: String,
                @Nullable  @SerializedName("endTime")
                val endTime: String,
                @Nullable  @SerializedName("flightNo")
                val flightNo: String,
                @Nullable  @SerializedName("fromCity")
                val fromCity: String,
                @Nullable  @SerializedName("imgUrl")
                val imgUrl: String,
                @Nullable  @SerializedName("startAirPortName")
                val startAirPortName: String,
                @Nullable  @SerializedName("startDate")
                val startDate: String,
                @Nullable  @SerializedName("startTime")
                val startTime: String,
                @Nullable  @SerializedName("stopCount")
                val stopCount: Int,
                @Nullable  @SerializedName("stopDetails")
                val stopDetails: ArrayList<StopDetail>,
                @Nullable  @SerializedName("toCity")
                val toCity: String
            ) : Parcelable {
                data class StopDetail(
                    @Nullable  @SerializedName("airline")
                    val airline: String,
                    @Nullable  @SerializedName("endAirPortName")
                    val endAirPortName: String,
                    @Nullable  @SerializedName("endDate")
                    val endDate: String,
                    @Nullable  @SerializedName("endTime")
                    val endTime: String,
                    @Nullable  @SerializedName("flightNo")
                    val flightNo: String,
                    @Nullable  @SerializedName("imgUrl")
                    val imgUrl: String,
                    @Nullable  @SerializedName("startAirPortName")
                    val startAirPortName: String,
                    @Nullable  @SerializedName("startDate")
                    val startDate: String,
                    @Nullable  @SerializedName("startTime")
                    val startTime: String
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
                        source.readString()
                    )

                    override fun describeContents() = 0

                    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                        writeString(airline)
                        writeString(endAirPortName)
                        writeString(endDate)
                        writeString(endTime)
                        writeString(flightNo)
                        writeString(imgUrl)
                        writeString(startAirPortName)
                        writeString(startDate)
                        writeString(startTime)
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
                    source.readInt(),
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
                    writeString(startAirPortName)
                    writeString(startDate)
                    writeString(startTime)
                    writeInt(stopCount)
                    writeTypedList(stopDetails)
                    writeString(toCity)
                }
            }

            data class OutbondFlightDetails(
                @Nullable  @SerializedName("airline")
                val airline: String,
                @Nullable  @SerializedName("currency")
                val currency: String,
                @Nullable  @SerializedName("duration")
                val duration: String,
                @Nullable  @SerializedName("endAirPortName")
                val endAirPortName: String,
                @Nullable  @SerializedName("endDate")
                val endDate: String,
                @Nullable  @SerializedName("endTime")
                val endTime: String,
                @Nullable  @SerializedName("flightNo")
                val flightNo: String,
                @Nullable  @SerializedName("fromCity")
                val fromCity: String,
                @Nullable  @SerializedName("imgUrl")
                val imgUrl: String,
                @Nullable  @SerializedName("startAirPortName")
                val startAirPortName: String,
                @Nullable  @SerializedName("startDate")
                val startDate: String,
                @Nullable  @SerializedName("startTime")
                val startTime: String,
                @Nullable  @SerializedName("stopCount")
                val stopCount: Int,
                @Nullable  @SerializedName("stopDetails")
                val stopDetails: List<StopDetail>,
                @Nullable  @SerializedName("toCity")
                val toCity: String
            ) : Parcelable {
                data class StopDetail(
                    @Nullable  @SerializedName("airline")
                    val airline: String,
                    @Nullable  @SerializedName("endAirPortName")
                    val endAirPortName: String,
                    @Nullable  @SerializedName("endDate")
                    val endDate: String,
                    @Nullable  @SerializedName("endTime")
                    val endTime: String,
                    @Nullable  @SerializedName("flightNo")
                    val flightNo: String,
                    @Nullable  @SerializedName("imgUrl")
                    val imgUrl: String,
                    @Nullable  @SerializedName("startAirPortName")
                    val startAirPortName: String,
                    @Nullable  @SerializedName("startDate")
                    val startDate: String,
                    @Nullable  @SerializedName("startTime")
                    val startTime: String
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
                        source.readString()
                    )

                    override fun describeContents() = 0

                    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                        writeString(airline)
                        writeString(endAirPortName)
                        writeString(endDate)
                        writeString(endTime)
                        writeString(flightNo)
                        writeString(imgUrl)
                        writeString(startAirPortName)
                        writeString(startDate)
                        writeString(startTime)
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
                    source.readInt(),
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
                    writeString(startAirPortName)
                    writeString(startDate)
                    writeString(startTime)
                    writeInt(stopCount)
                    writeTypedList(stopDetails)
                    writeString(toCity)
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
                source.readString(),
                source.readString(),
                source.readParcelable<InbondFlightDetails>(InbondFlightDetails::class.java.classLoader),
                source.readParcelable<OutbondFlightDetails>(OutbondFlightDetails::class.java.classLoader),
                source.readDouble()
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(currency)
                writeString(dataProvider)
                writeParcelable(inbondFlightDetails, 0)
                writeParcelable(outbondFlightDetails, 0)
                writeDouble(price)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<FlightListResponse> = object : Parcelable.Creator<FlightListResponse> {
                override fun createFromParcel(source: Parcel): FlightListResponse = FlightListResponse(source)
                override fun newArray(size: Int): Array<FlightListResponse?> = arrayOfNulls(size)
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

    data class AirPortDataResponse(
        @Nullable   @SerializedName("message") val message: String?,
        @Nullable  @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @Nullable  @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @Nullable  @SerializedName("airPortCode")
            val airPortCode: String,
            @Nullable  @SerializedName("airPortCountry")
            val airPortCountry: String,
            @Nullable  @SerializedName("airPortId")
            val airPortId: Int,
            @Nullable  @SerializedName("airPortName")
            val airPortName: String,
            @Nullable  @SerializedName("airPortNameAndCode")
            val airPortNameAndCode: String
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
        val stopCount: Int?,
        val stopDetailsInBound: ArrayList<FlightListResponse.ResponseData.InbondFlightDetails.StopDetail>?,
        val stopDetailsOutBound: List<FlightListResponse.ResponseData.OutbondFlightDetails.StopDetail>?,
        val toCity: String?

    )

    data class StopDetail(
        val duration: String,
        val price: String,
        val currency: String,
        var airline: String,
        val endAirPortName: String,
        val endDate: String,
        val endTime: String,
        val flightNo: String,
        val imgUrl: String,
        val startAirPortName: String,
        val startDate: String,
        val startTime: String
    )

}