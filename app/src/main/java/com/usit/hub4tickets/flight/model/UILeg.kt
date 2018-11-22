package com.usit.hub4tickets.flight.model

import android.content.Context
import com.usit.hub4tickets.R
import java.text.SimpleDateFormat

/**
 * Created by Luis on 01/10/2017.
 */
class UILeg(
    val carrierIcon: String,
    val carrierName: String,
    val originStationName: String,
    val destinationStationName: String,
    val departure: String,
    val arrival: String,
    val scales: Int,
    val duration: Int
) {
    fun departureAndArrival(): String {
        var formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val arrivalDate = formatter.parse(arrival)
        val departureDate = formatter.parse(departure)

        formatter = SimpleDateFormat("HH:mm")
        val arrival = formatter.format(arrivalDate)
        val departure = formatter.format(departureDate)

        return "${departure} - ${arrival}"
    }

    fun originDestinationAndCarrier(): String = "${originStationName}-${destinationStationName}, ${carrierName}"

    fun scalesVerbose(context: Context): String {
        if (scales == 0) {
            return context.getString(R.string.direct)
        } else {
            return context.getString(R.string.n_scales).format(scales)
        }
    }

    fun durationVerbose(): String {
        return "2h 25m"
    }
}