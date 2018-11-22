package com.usit.hub4tickets.domain.presentation.presenters

import com.usit.hub4tickets.dashboard.model.DashboardViewModel

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface DashboardPresenter : BasePresenter {
    interface MainView {
        /**
         * This enum is used for determine the current state of this screen
         */
        enum class ViewState {
            IDLE, LOADING, LOAD_SIGN_UP, SUCCESS, ERROR, LANG_SUCCESS, CURRENCY_SUCCESS, COUNTRY_SUCCESS, SAVE_SUCCESS,
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
        fun doRetrieveModel(): DashboardViewModel

    }

    /**
     * This method is used for present the current state of this screen
     *
     * @param state
     */
    fun presentState(state: MainView.ViewState)

    fun callAPIGetSettingsData(userId: String)

    fun callAPISaveSettingsData(
        userId: String,
        countryId: String,
        currencyId: String,
        langId: String
    )

    fun callAPIGetCountry()
    fun callAPIGetCurrency()
    fun callAPIGetLanguage()
}
