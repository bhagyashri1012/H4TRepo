package com.usit.hub4tickets.login

import com.usit.hub4tickets.api.network.ErrorResponse
import com.usit.hub4tickets.domain.api.APICallManager
import com.usit.hub4tickets.domain.api.AirportDataAPICallListener
import com.usit.hub4tickets.utils.Enums
import com.usit.hub4tickets.utils.presentation.presenters.BaseInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class FlightBaseInteractor(private var listenerAirportDataAPICallListener: AirportDataAPICallListener) :
    BaseInteractor {

    fun callAPIAirportData(flag: String?,filterValue:String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getAirportData(filterValue)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerAirportDataAPICallListener.onAPICallSucceed(route, response, flag)
            },
            { error ->
                listenerAirportDataAPICallListener.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIFlightDetails(
        adults: String,
        cabinClass: String,
        children: String,
        dateFrom: String,
        flightType: String,
        flyFrom: String,
        flyTo: String,
        infants: String,
        locale: String,
        returnFrom: String
    ) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getFlightDetails(
            adults,
            cabinClass,
            children,
            dateFrom,
            flightType,
            flyFrom,
            flyTo,
            infants,
            locale,
            returnFrom
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerAirportDataAPICallListener.onAPICallFlightDetailsSucceed(route, response)
            },
            { error ->
                listenerAirportDataAPICallListener.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }
}