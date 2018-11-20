package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import android.provider.Settings.Secure
import android.util.Log
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.model.DashboardViewModel
import com.usit.hub4tickets.domain.api.DashboardAPICallListener
import com.usit.hub4tickets.domain.presentation.presenters.DashboardPresenter
import com.usit.hub4tickets.domain.presentation.presenters.DashboardPresenter.MainView.ViewState.*
import com.usit.hub4tickets.login.DashboardBaseInteractor
import com.usit.hub4tickets.profile.ui.PersonalInfoActivity
import com.usit.hub4tickets.utils.Enums
import com.usit.hub4tickets.utils.Utility

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class DashboardPresenterImpl(
    private val mView: DashboardPresenter.MainView,
    context: Context?
) : DashboardPresenter, DashboardAPICallListener {

    private val dashboardBaseInteractor: DashboardBaseInteractor =
        DashboardBaseInteractor(this)
    private val mContext = context
    private var device_id = Secure.getString(mContext?.contentResolver, Secure.ANDROID_ID)

    override fun callAPIGetProfile(userId: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetPersonalInfo(
                device_id,
                userId
            )
        } else {
            mView.doRetrieveModel().errorMessage =
                    mView.doRetrieveModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetCountry() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetCountry()
        } else {
            mView.doRetrieveModel().errorMessage =
                    mView.doRetrieveModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetCurrency() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetCurrency()
        } else {
            mView.doRetrieveModel().errorMessage =
                    mView.doRetrieveModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIGetLanguage() {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            dashboardBaseInteractor.callAPIGetLanguage()
        } else {
            mView.doRetrieveModel().errorMessage =
                    mView.doRetrieveModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun presentState(state: DashboardPresenter.MainView.ViewState) {
        // user state logging
        Log.i(PersonalInfoActivity::class.java.simpleName, state.name)
        when (state) {
            IDLE -> mView.showState(IDLE)
            LOADING -> mView.showState(LOADING)
            SUCCESS -> mView.showState(SUCCESS)
            LANG_SUCCESS -> mView.showState(LANG_SUCCESS)
            CURRENCY_SUCCESS -> mView.showState(CURRENCY_SUCCESS)
            COUNTRY_SUCCESS -> mView.showState(COUNTRY_SUCCESS)
            ERROR -> mView.showState(ERROR)
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
                mView.doRetrieveModel().settingsDomain = responseModel
                presentState(SUCCESS)
            }
        }

    override fun onAPICallGetCountrySucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.CountriesResponse
    ) {
        when (route) {

            Enums.APIRoute.GET_SAMPLE -> {
                mView.doRetrieveModel().dashboradCountriesDomain = responseModel
                presentState(COUNTRY_SUCCESS)
            }
        }
    }

    override fun onAPICallGetLanguageSucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.LanguageResponse
    ) {
        when (route) {

            Enums.APIRoute.GET_SAMPLE -> {
                mView.doRetrieveModel().dashboradLangDomain = responseModel
                presentState(LANG_SUCCESS)
            }
        }
    }

    override fun onAPICallGetCurrencySucceed(
        route: Enums.APIRoute,
        responseModel: DashboardViewModel.CurrencyResponse
    ) {
        when (route) {

            Enums.APIRoute.GET_SAMPLE -> {
                mView.doRetrieveModel().dashboradCurrencyDomain = responseModel
                presentState(CURRENCY_SUCCESS)
            }
        }
    }

    override fun onAPICallFailed(route: Enums.APIRoute, message: String?) {
        Utility.hideProgressBar()
        mView.doRetrieveModel().errorMessage = message
        presentState(ERROR)
    }
}
