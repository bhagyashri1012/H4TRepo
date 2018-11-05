package com.usit.hub4tickets.flight.model

import android.content.Context
import com.usit.hub4tickets.R

/**
 * Created by Luis on 01/10/2017.
 */
class UIItinerary(
    val outboundLeg : UILeg,
    val inboundLeg : UILeg,
    val price : String,
    val agent : String
) {
    fun agentVerbose(context : Context) : String {
        return context.getString(R.string.via_agent).format(agent)
    }
}