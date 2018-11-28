package com.usit.hub4tickets.flight.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class FlightViewModel(var context: Context?) {
    var errorMessage: ErrorResponse = FlightViewModel.ErrorResponse(message = "")
    var flightViewModel: FlightViewModel.AirPortDataResponse =
        FlightViewModel.AirPortDataResponse(message = null, responseData = null, status = null)

    data class ErrorResponse(
        @SerializedName("message")
        var message: String
    )

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


}