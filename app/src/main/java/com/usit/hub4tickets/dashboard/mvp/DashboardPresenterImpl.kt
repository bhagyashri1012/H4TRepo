package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import android.util.Log
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.account.ui.PersonalInfoActivity
import com.usit.hub4tickets.dashboard.model.DashboardViewModel
import com.usit.hub4tickets.domain.api.DashboardAPICallListener
import com.usit.hub4tickets.domain.presentation.presenters.DashboardPresenter
import com.usit.hub4tickets.domain.presentation.presenters.DashboardPresenter.MainView.ViewState.*
import com.usit.hub4tickets.login.DashboardBaseInteractor
import com.usit.hub4tickets.utils.Enums
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class DashboardPresenterImpl(
    private val mView: DashboardPresenter.MainView?,
    context: Context?
) : DashboardPresenter, DashboardAPICallListener {

    private val dashboardBaseInteractor: DashboardBaseInteractor =
        DashboardBaseInteractor(this)
    private val mContext = context

    override fun callAPIGetSettingsData(userId: String) {
        if (MainApplication.getInstance.isConnected()) {
            dashboardBaseInteractor.callAPIGetSettingsData(
                Utility.getDeviceId(context = mContext),
                userId,
                Pref.getValue(mContext, PrefConstants.DEFAULT_LOCATION, "")!!,
                Pref.getValue(mContext, PrefConstants.DEFAULT_LANGUAGE, "")!!
            )
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPISaveSettingsData(userId: String, countryId: String, currencyId: String, langId: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPISaveSettingsData(
                userId,
                Utility.getDeviceId(context = mContext),
                countryId,
                currencyId,
                langId
            )
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetCountry() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetCountry()
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetState(countryId: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetState(countryId)
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetCity(stateId: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetCity(stateId)
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetCurrency() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetCurrency()
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetLanguage() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetLanguage()
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPITimeZone() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetTimeZone()
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIAirports() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetAirports()
        } else {
            mView?.doRetrieveModel()?.errorMessage =
                    mView?.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun presentState(state: DashboardPresenter.MainView.ViewState) {
        // user state logging
        Log.i(PersonalInfoActivity::class.java.simpleName, state.name)
        when (state) {
            IDLE -> mView?.showState(IDLE)
            LOADING -> mView?.showState(LOADING)
            SUCCESS -> mView?.showState(SUCCESS)
            SAVE_SUCCESS -> mView?.showState(SAVE_SUCCESS)
            LANG_SUCCESS -> mView?.showState(LANG_SUCCESS)
            CURRENCY_SUCCESS -> mView?.showState(CURRENCY_SUCCESS)
            COUNTRY_SUCCESS -> mView?.showState(COUNTRY_SUCCESS)
            STATE_SUCCESS -> mView?.showState(STATE_SUCCESS)
            CITY_SUCCESS -> mView?.showState(CITY_SUCCESS)
            TIME_ZONE_SUCCESS -> mView?.showState(TIME_ZONE_SUCCESS)
            AIRPORTS_SUCCESS -> mView?.showState(AIRPORTS_SUCCESS)
            ERROR -> mView?.showState(ERROR)
        }
    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {

    }

    override fun onAPICallSucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.SettingsResponse) =
        when (route) {

            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.settingsDomain = responseModel
                presentState(SUCCESS)
                presentState(IDLE)
            }
        }

    override fun onAPICallSaveSucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.SettingsResponse) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                if (responseModel.status.equals("1")) {
                    mView?.doRetrieveModel()?.settingsDomain = responseModel
                    Utility.showCustomDialog(
                        mContext,
                        responseModel.message,
                        "",
                        object : CustomDialogPresenter.CustomDialogView {
                            override fun onPositiveButtonClicked() {
                                presentState(SAVE_SUCCESS)
                            }

                            override fun onNegativeButtonClicked() {
                            }
                        })
                }
            }
        }
    }

    override fun onAPICallGetCountrySucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.CountriesResponse
    ) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.dashboradCountriesDomain = responseModel
                presentState(COUNTRY_SUCCESS)
            }
        }
    }

    override fun onAPICallGetStateSucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.StateResponse) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.dashboradStateDomain = responseModel
                presentState(STATE_SUCCESS)
            }
        }
    }

    override fun onAPICallGetCitySucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.CityResponse) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.dashboradCityDomain = responseModel
                presentState(CITY_SUCCESS)
            }
        }
    }

    override fun onAPICallGetLanguageSucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.LanguageResponse
    ) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.dashboradLangDomain = responseModel
                presentState(LANG_SUCCESS)
            }
        }
    }

    override fun onAPICallGetTimeZoneSucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.LanguageResponse
    ) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.dashboradLangDomain = responseModel
                presentState(TIME_ZONE_SUCCESS)
            }
        }
    }

    override fun onAPICallGetAirportsSucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.LanguageResponse
    ) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.dashboradLangDomain = responseModel
                presentState(AIRPORTS_SUCCESS)
            }
        }
    }

    override fun onAPICallGetCurrencySucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.CurrencyResponse
    ) {
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {
                mView?.doRetrieveModel()?.dashboradCurrencyDomain = responseModel
                presentState(CURRENCY_SUCCESS)
            }
        }
    }

    override fun onAPICallFailed(route: Enums.APIRoute, message: String) {
        Utility.hideProgressBar()
        mView?.doRetrieveModel()?.errorMessage = message
        presentState(ERROR)
    }
}
