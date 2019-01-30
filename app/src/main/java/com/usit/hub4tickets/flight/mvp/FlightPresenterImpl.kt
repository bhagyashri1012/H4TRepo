package com.usit.hub4tickets.domain.presentation.screens.mainimport android.content.Contextimport com.usit.hub4tickets.MainApplicationimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.domain.api.AirportDataAPICallListenerimport com.usit.hub4tickets.domain.presentation.presenters.FlightPresenterimport com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter.MainView.ViewState.*import com.usit.hub4tickets.flight.model.FlightViewModelimport com.usit.hub4tickets.login.FlightBaseInteractorimport com.usit.hub4tickets.utils.Constantimport com.usit.hub4tickets.utils.Enumsimport com.usit.hub4tickets.utils.Utility/** * Created by Bhagyashri Burade * Date: 24/10/2018 * Email: bhagyashri.burade@usit.net.in */class FlightPresenterImpl(    private val mView: FlightPresenter.MainView,    context: Context?) : FlightPresenter, AirportDataAPICallListener {    override fun onAPICallMulticitySucceed(route: Enums.APIRoute, responseModel: FlightViewModel.MultiCityResponse) {        when (route) {            Enums.APIRoute.GET_SAMPLE -> {                if (responseModel.status?.equals(Constant.Path.SUCCESS)!!) {                    presentState(IDLE)                    mView.doRetrieveModel().multiCityListViewModel = responseModel                    if (responseModel.responseData?.isNotEmpty()!!) {                        presentState(MULTICITY_DETAILS_SUCCESS)                    } else {                        presentState(FLIGHT_NOT_FOUND)                    }                } else {                    mView.doRetrieveModel().errorMessage.message = responseModel.message.toString()                    presentState(ERROR)                }            }        }    }    override fun callMulticityDetails(        adults: String,        children: String,        curr: String,        deviceId: String,        infants: String,        locale: String,        multiCitiesToSearch: List<FlightViewModel.MultiCitiesForSearch>?,        travel_class: String,        userId: String    ) {        if (MainApplication.getInstance.isConnected()) {            presentState(LOADING)            flightPresenterImpl.callMultiCity(                adults,                children,                curr,                Utility.getDeviceId(context = mContext),                infants,                locale,                multiCitiesToSearch,                travel_class,                userId            )        } else {            mView.doRetrieveModel().errorMessage.message =                mView.doRetrieveModel().context!!.getString(R.string.message_no_internet)            presentState(ERROR)        }    }    private val flightPresenterImpl: FlightBaseInteractor =        FlightBaseInteractor(this)    private val mContext = context    override fun callFlightDetails(        userId: String,        adults: String,        cabinClass: String,        children: String,        dateFrom: String,        flightType: String,        flyFrom: String,        flyTo: String,        infants: String,        locale: String,        returnFrom: String,        curr: String,        price_from: String?,        price_to: String?,        dtime_from: String?,        dtime_to: String?,        atime_from: String?,        atime_to: String?,        max_stopovers: ArrayList<Int>?    ) {        if (MainApplication.getInstance.isConnected()) {            presentState(LOADING)            flightPresenterImpl.callAPIFlightDetails(                userId,                Utility.getDeviceId(context = mContext),                adults,                cabinClass,                children,                dateFrom,                flightType,                flyFrom,                flyTo,                infants,                locale,                returnFrom,                curr,                price_from,                price_to,                dtime_from,                dtime_to,                atime_from,                atime_to,                max_stopovers            )        } else {            mView.doRetrieveModel().errorMessage.message =                mView.doRetrieveModel().context!!.getString(R.string.message_no_internet)            presentState(ERROR)        }    }    override fun callAPIAirportData(flag: String?, toString: String) {        if (MainApplication.getInstance.isConnected()) {            presentState(LOADING)            flightPresenterImpl.callAPIAirportData(flag, toString)        } else {            mView.doRetrieveModel().errorMessage.message =                mView.doRetrieveModel().context!!.getString(R.string.message_no_internet)            presentState(ERROR)        }    }    override fun presentState(state: FlightPresenter.MainView.ViewState) {        // user state logging        when (state) {            IDLE -> mView.showState(IDLE)            LOADING -> mView.showState(LOADING)            SUCCESS_FROM -> mView.showState(SUCCESS_FROM)            SUCCESS_TO -> mView.showState(SUCCESS_TO)            FLIGHT_DETAILS_SUCCESS -> mView.showState(FLIGHT_DETAILS_SUCCESS)            MULTICITY_DETAILS_SUCCESS -> mView.showState(MULTICITY_DETAILS_SUCCESS)            FLIGHT_NOT_FOUND -> mView.showState(FLIGHT_NOT_FOUND)            ERROR -> mView.showState(ERROR)        }    }    override fun onAPICallSucceed(        route: Enums.APIRoute,        responseModel: FlightViewModel.AirPortDataResponse,        flag: String?    ) =        when (route) {            Enums.APIRoute.GET_SAMPLE -> {                presentState(IDLE)                mView.doRetrieveModel().flightViewModel = responseModel                if (flag.equals("FROM"))                    presentState(SUCCESS_FROM)                else                    presentState(SUCCESS_TO)            }        }    override fun onAPICallFlightDetailsSucceed(        route: Enums.APIRoute,        responseModel: FlightViewModel.FlightListResponse    ) {        when (route) {            Enums.APIRoute.GET_SAMPLE -> {                if (responseModel.status?.equals(Constant.Path.SUCCESS)!!) {                    presentState(IDLE)                    mView.doRetrieveModel().flightListViewModel = responseModel                    if (responseModel.responseData?.isNotEmpty()!!) {                        presentState(FLIGHT_DETAILS_SUCCESS)                    } else {                        presentState(FLIGHT_NOT_FOUND)                    }                } else {                    mView.doRetrieveModel().errorMessage.message = responseModel.message.toString()                    presentState(ERROR)                }            }        }    }    override fun onAPICallFailed(route: Enums.APIRoute, message: String) {        Utility.hideProgressBar()        mView.doRetrieveModel().errorMessage.message = message        presentState(ERROR)    }    override fun resume() {    }    override fun pause() {    }    override fun stop() {    }    override fun destroy() {    }}