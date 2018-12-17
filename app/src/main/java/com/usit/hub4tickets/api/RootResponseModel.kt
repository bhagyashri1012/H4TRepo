package com.usit.hub4tickets.domain.api

import com.google.gson.annotations.SerializedName
import io.reactivex.annotations.Nullable

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
data class RootResponseModel(
    @Nullable
    @SerializedName("details")
    val details: String?,
    @Nullable
    @SerializedName("message")
    var message: String?,
    @Nullable
    @SerializedName("status")
    val status: String?,
    @Nullable
    @SerializedName("timeStamp")
    val timeStamp: String?
)
