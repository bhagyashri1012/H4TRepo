package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import android.provider.Settings.Secure
import android.support.v7.app.AlertDialog
import android.util.Log
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.account.ui.PersonalInfoActivity
import com.usit.hub4tickets.domain.api.ProfileInfoAPICallListener
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter.MainView.ViewState.*
import com.usit.hub4tickets.login.ProfileBaseInteractor
import com.usit.hub4tickets.utils.Enums
import com.usit.hub4tickets.utils.Utility
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class ProfilePresenterImpl(
    private val mView: ProfilePresenter.MainView,
    context: Context
) : ProfilePresenter, ProfileInfoAPICallListener {
    override fun callAPIUpdateProfile(
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
    ) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            profileBaseInteractor.callAPIUpdatePersonalInfo(
                userId,
                device_id,
                email,
                firstname,
                lastname,
                phonenumber,
                homeairport,
                timezoneId,
                countryId,
                stateId,
                cityId,
                languageId,
                check
            )
        } else {
            mView.doRetrieveProfileModel().errorMessage =
                    mView.doRetrieveProfileModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    private val profileBaseInteractor: ProfileBaseInteractor =
        ProfileBaseInteractor(this)
    private val mContext = context
    private var device_id = Secure.getString(mContext.contentResolver, Secure.ANDROID_ID)

    override fun callAPIGetProfile(userId: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            profileBaseInteractor.callAPIGetPersonalInfo(
                device_id,
                userId
            )
        } else {
            mView.doRetrieveProfileModel().errorMessage =
                    mView.doRetrieveProfileModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callAPIChangePassword(
        dialogView: AlertDialog,
        userId: String,
        deviceId: String,
        oldPassword: String,
        newPassword: String
    ) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            profileBaseInteractor.callAPIResetPassword(dialogView, userId, deviceId, oldPassword, newPassword)
        } else {
            mView.doRetrieveProfileModel().errorMessage =
                    mView.doRetrieveProfileModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun presentState(state: ProfilePresenter.MainView.ViewState) {
        // user state logging
        Log.i(PersonalInfoActivity::class.java.simpleName, state.name)
        when (state) {
            IDLE -> mView.showState(IDLE)
            LOADING -> mView.showState(LOADING)
            SUCCESS -> mView.showState(SUCCESS)
            ERROR -> mView.showState(ERROR)
            UPDATE_SUCCESS -> mView.showState(UPDATE_SUCCESS)
            CHANGE_PASSWORD_SUCCESS -> mView.showState(CHANGE_PASSWORD_SUCCESS)
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

    override fun onAPICallSucceed(route: Enums.APIRoute, responseModel: ProfileViewModel.ProfileResponse) =
        when (route) {
            Enums.APIRoute.GET_SAMPLE -> {

                mView.doRetrieveProfileModel().profileDomain = responseModel
                presentState(SUCCESS)
            }
        }

    override fun onAPICallUpdatePersonalInfoSucceed(
        route: Enums.APIRoute,
        responseModel: ProfileViewModel.ProfileResponse
    ) =
        when (route) {

            Enums.APIRoute.GET_SAMPLE -> {
                CustomDialogPresenter.showDialog(mContext,
                    "",
                    responseModel.message,
                    mContext.resources.getString(
                        R.string.ok
                    ),
                    null,
                    object : CustomDialogPresenter.CustomDialogView {
                        override fun onPositiveButtonClicked() {
                            presentState(UPDATE_SUCCESS)
                        }

                        override fun onNegativeButtonClicked() {
                        }
                    })
            }
        }

    override fun onAPICallChangePasswordSucceed(
        route: Enums.APIRoute,
        responseModel: ProfileViewModel.ProfileResponse,
        dialogView: AlertDialog
    ) {
        CustomDialogPresenter.showDialog(mContext,
            "",
            responseModel.message,
            mContext.resources.getString(
                R.string.ok
            ),
            null,
            object : CustomDialogPresenter.CustomDialogView {
                override fun onPositiveButtonClicked() {
                    presentState(CHANGE_PASSWORD_SUCCESS)
                    dialogView.dismiss()
                }

                override fun onNegativeButtonClicked() {
                }
            })
    }

    override fun onAPICallFailed(route: Enums.APIRoute, message: String) {
        mView.doRetrieveProfileModel().errorMessage = message
        presentState(IDLE)
    }
}
