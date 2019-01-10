package com.usit.hub4tickets.domain.presentation.presenters

import android.support.v7.app.AlertDialog
import com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModel


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface ProfilePresenter : BasePresenter {
    interface MainView {
        /**
         * This enum is used for determine the current state of this screen
         */
        enum class ViewState {
            IDLE, LOADING, LOAD_SIGN_UP, SUCCESS, ERROR, UPDATE_SUCCESS, CHANGE_PASSWORD_SUCCESS, SETTING_DATA_SUCCESS,
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
        fun doRetrieveProfileModel(): ProfileViewModel

    }

    /**
     * This method is used for present the current state of this screen
     *
     * @param state
     */
    fun presentState(state: MainView.ViewState)

    fun callAPIGetSettingsData(userId: String)

    fun callAPIGetProfile(userId: String)

    fun callAPIUpdateProfile(
        userId: String,
        email: String,
        firstname: String,
        lastname: String,
        phonenumber: String,
        homeairport: String,
        timezoneId: String,
        countryId: String,
        stateId: String,
        cityId: String,
        languageId: String,
        check: String
    )

    fun callAPIChangePassword(
        dialogView: AlertDialog,
        userId: String,
        deviceId: String,
        oldPassword: String,
        newPassword: String
    )

}
