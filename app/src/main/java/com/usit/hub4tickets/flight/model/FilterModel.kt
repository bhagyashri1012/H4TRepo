package com.usit.hub4tickets.flight.model

import android.os.Parcel
import android.os.Parcelable

class FilterModel {
    data class Filter(
        var price_from: String,
        var price_to: String,
        var dtime_from: String?,
        var dtime_to: String?,
        var atime_from: String,
        var atime_to: String,
        var max_stopovers: ArrayList<Int>
    ) : Parcelable {
        constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            ArrayList<Int>().apply { source.readList(this, Int::class.java.classLoader) }
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(price_from)
            writeString(price_to)
            writeString(dtime_from)
            writeString(dtime_to)
            writeString(atime_from)
            writeString(atime_to)
            writeList(max_stopovers)
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