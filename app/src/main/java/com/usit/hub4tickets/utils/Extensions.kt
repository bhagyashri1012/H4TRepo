@file:JvmName("ExtensionsUtils")

package com.usit.hub4tickets.utils

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
inline fun <reified T : Parcelable> createParcel(
    crossinline createFromParcel: (Parcel) -> T?
): Parcelable.Creator<T> =
    object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
        override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
    }