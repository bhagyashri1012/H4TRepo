package com.usit.hub4tickets.flight.model

import android.os.Parcel
import android.os.Parcelable

class FilterModel {
    data class Filter(
        val price_from: String,
        val price_to: String,
        val dtime_from: String,
        val dtime_to: String,
        val atime_from: String,
        val atime_to: String,
        var max_stopovers: String
    ) : Parcelable {
        constructor(source: Parcel) : this(
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
            writeString(price_from)
            writeString(price_to)
            writeString(dtime_from)
            writeString(dtime_to)
            writeString(atime_from)
            writeString(atime_to)
            writeString(max_stopovers)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Filter> = object : Parcelable.Creator<Filter> {
                override fun createFromParcel(source: Parcel): Filter = Filter(source)
                override fun newArray(size: Int): Array<Filter?> = arrayOfNulls(size)
            }
        }
    }
}