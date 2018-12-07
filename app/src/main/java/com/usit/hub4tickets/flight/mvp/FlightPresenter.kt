package com.usit.hub4tickets.domain.presentation.presenters

import com.usit.hub4tickets.flight.model.FlightViewModel

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface FlightPresenter : BasePresenter {
    interface MainView {
        /**
         * This enum is used for determine the current state of this screen
         */
        enum class ViewState {
            IDLE, LOADING, LOAD_SIGN_UP, SUCCESS_FROM, SUCCESS_TO, ERROR, FLIGHT_DETAILS_SUCCESS,
        }

        /**
         * This method is to show the current state of this screen
         *
         * @param viewState
         */
        fun showState(viewState: ViewState)

        /**
         * This function return the model that was belong to this screen
         *
         * @return
         */
        fun doRetrieveModel(): FlightViewModel

    }

    /**
     * This method is used for present the current state of this screen
     *
     * @param state
     */
    fun presentState(state: MainView.ViewState)

    fun callAPIAirportData(flag: String?, toString: String)

    fun callFlightDetails(
        adults: String,
        travel_class: String,
        children: String,
        dateFrom: String,
        flightType: String,
        flyFrom: String,
        flyTo: String,
        infants: String,
        locale: String,
        returnFrom: String
    )
}