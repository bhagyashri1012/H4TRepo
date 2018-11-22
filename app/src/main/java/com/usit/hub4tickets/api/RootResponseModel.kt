package com.usit.hub4tickets.domain.api

import com.google.gson.annotations.SerializedName

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
data class RootResponseModel(
    @SerializedName("details")
    val details: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("timeStamp")
    val timeStamp: String?
)
