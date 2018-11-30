package com.usit.hub4tickets.domain.api

import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.Enums

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface AirportDataAPICallListener {
    fun onAPICallSucceed(
        route: Enums.APIRoute,
        responseModel: FlightViewModel.AirPortDataResponse,
        flag: String?
    )

    fun onAPICallFlightDetailsSucceed(
        route: Enums.APIRoute,
        responseModel: FlightViewModel.FlightListResponse
    )

    fun onAPICallFailed(route: Enums.APIRoute, message: String)
}
