package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.api.AirportDataAPICallListener
import com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter
import com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter.MainView.ViewState.*
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.login.FlightBaseInteractor
import com.usit.hub4tickets.utils.Enums
import com.usit.hub4tickets.utils.Utility

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class FlightPresenterImpl(
    private val mView: FlightPresenter.MainView,
    context: Context?
) : FlightPresenter, AirportDataAPICallListener {

    private val flightPresenterImpl: FlightBaseInteractor =
        FlightBaseInteractor(this)
    private val mContext = context

    override fun callFlightDetails(
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
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            flightPresenterImpl.callAPIFlightDetails(
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
        } else {
            mView.doRetrieveModel().errorMessage.message =
                    mView.doRetrieveModel().context!!.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIAirportData(flag: String?, toString: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            flightPresenterImpl.callAPIAirportData(flag, toString)
        } else {
            mView.doRetrieveModel().errorMessage.message =
                    mView.doRetrieveModel().context!!.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun presentState(state: FlightPresenter.MainView.ViewState) {
        // user state logging
        when (state) {
            IDLE -> mView.showState(IDLE)
            LOADING -> mView.showState(LOADING)
            SUCCESS_FROM -> mView.showState(SUCCESS_FROM)
            SUCCESS_TO -> mView.showState(SUCCESS_TO)
            FLIGHT_DETAILS_SUCCESS -> mView.showState(FLIGHT_DETAILS_SUCCESS)
            ERROR -> mView.showState(ERROR)
        }
    }

    override fun onAPICallSucceed(
        route: Enums.APIRoute,
        responseModel: FlightViewModel.AirPortDataResponse,
        flag: String?
    ) =
        when (route) {

            Enums.APIRoute.GET_SAMPLE -> {
                mView.doRetrieveModel().flightViewModel = responseModel
                if (flag.equals("FROM"))
                    presentState(SUCCESS_FROM)
                else
                    presentState(SUCCESS_TO)
            }
        }

    override fun onAPICallFlightDetailsSucceed(
        route: Enums.APIRoute,
        responseModel: FlightViewModel.FlightListResponse
    ) {
        when (route) {

            Enums.APIRoute.GET_SAMPLE -> {
                if (responseModel.responseData?.size!! > 0) {
                    presentState(IDLE)
                    mView.doRetrieveModel().flightListViewModel = responseModel
                    presentState(FLIGHT_DETAILS_SUCCESS)
                } else {
                    mView.doRetrieveModel().errorMessage.message = responseModel.message.toString()
                    presentState(ERROR)
                }
            }
        }
    }

    override fun onAPICallFailed(route: Enums.APIRoute, message: String) {
        Utility.hideProgressBar()
        mView.doRetrieveModel().errorMessage.message = message
        presentState(ERROR)
    }

    override fun resume() {

    }


    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {

    }

}
