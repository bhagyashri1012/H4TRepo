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
        @SerializedName("message")
        var message: String
    )

    data class FlightListResponse(
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("currency")
            val currency: String,
            @SerializedName("dataProvider")
            val dataProvider: String,

            @Nullable
            @SerializedName("inbondFlightDetails")
            val inbondFlightDetails: InbondFlightDetails?,
            @SerializedName("outbondFlightDetails")
            val outbondFlightDetails: OutbondFlightDetails?,
            @SerializedName("price")
            val price: Double
        ) : Parcelable {
            data class InbondFlightDetails(
                @SerializedName("airline")
                val airline: String,
                @SerializedName("currency")
                val currency: String,
                @SerializedName("duration")
                val duration: String,
                @SerializedName("endAirPortName")
                val endAirPortName: String,
                @SerializedName("endDate")
                val endDate: String,
                @SerializedName("endTime")
                val endTime: String,
                @SerializedName("flightNo")
                val flightNo: String,
                @SerializedName("fromCity")
                val fromCity: String,
                @SerializedName("imgUrl")
                val imgUrl: String,
                @SerializedName("startAirPortName")
                val startAirPortName: String,
                @SerializedName("startDate")
                val startDate: String,
                @SerializedName("startTime")
                val startTime: String,
                @SerializedName("stopCount")
                val stopCount: Int,
                @SerializedName("stopDetails")
                val stopDetails: ArrayList<StopDetail>,
                @SerializedName("toCity")
                val toCity: String
            ) : Parcelable {
                data class StopDetail(
                    @SerializedName("airline")
                    val airline: String,
                    @SerializedName("endAirPortName")
                    val endAirPortName: String,
                    @SerializedName("endDate")
                    val endDate: String,
                    @SerializedName("endTime")
                    val endTime: String,
                    @SerializedName("flightNo")
                    val flightNo: String,
                    @SerializedName("imgUrl")
                    val imgUrl: String,
                    @SerializedName("startAirPortName")
                    val startAirPortName: String,
                    @SerializedName("startDate")
                    val startDate: String,
                    @SerializedName("startTime")
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
                @SerializedName("airline")
                val airline: String,
                @SerializedName("currency")
                val currency: String,
                @SerializedName("duration")
                val duration: String,
                @SerializedName("endAirPortName")
                val endAirPortName: String,
                @SerializedName("endDate")
                val endDate: String,
                @SerializedName("endTime")
                val endTime: String,
                @SerializedName("flightNo")
                val flightNo: String,
                @SerializedName("fromCity")
                val fromCity: String,
                @SerializedName("imgUrl")
                val imgUrl: String,
                @SerializedName("startAirPortName")
                val startAirPortName: String,
                @SerializedName("startDate")
                val startDate: String,
                @SerializedName("startTime")
                val startTime: String,
                @SerializedName("stopCount")
                val stopCount: Int,
                @SerializedName("stopDetails")
                val stopDetails: List<StopDetail>,
                @SerializedName("toCity")
                val toCity: String
            ) : Parcelable {
                data class StopDetail(
                    @SerializedName("airline")
                    val airline: String,
                    @SerializedName("endAirPortName")
                    val endAirPortName: String,
                    @SerializedName("endDate")
                    val endDate: String,
                    @SerializedName("endTime")
                    val endTime: String,
                    @SerializedName("flightNo")
                    val flightNo: String,
                    @SerializedName("imgUrl")
                    val imgUrl: String,
                    @SerializedName("startAirPortName")
                    val startAirPortName: String,
                    @SerializedName("startDate")
                    val startDate: String,
                    @SerializedName("startTime")
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
        @SerializedName("message")
        val message: String?,
        @SerializedName("responseData")
        val responseData: List<ResponseData>?,
        @SerializedName("status")
        val status: String?
    ) : Parcelable {
        data class ResponseData(
            @SerializedName("airPortCode")
            val airPortCode: String,
            @SerializedName("airPortId")
            val airPortId: Int,
            @SerializedName("airPortName")
            val airPortName: String
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
                writeString(airPortCode)
                writeInt(airPortId)
                writeString(airPortName)
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
        val airline: String,
        val currency: String,
        val duration: String,
        val price: String,
        val endAirPortName: String,
        val endDate: String,
        val endTime: String,
        val flightNo: String,
        val fromCity: String,
        val imgUrl: String,
        val startAirPortName: String,
        val startDate: String,
        val startTime: String,
        val stopCount: Int,
        val stopDetailsInBound: ArrayList<FlightListResponse.ResponseData.InbondFlightDetails.StopDetail>?,
        val stopDetailsOutBound: List<FlightListResponse.ResponseData.OutbondFlightDetails.StopDetail>?,
        val toCity: String

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