package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import android.util.Log
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.api.LoginAPICallListener
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter.MainView.ViewState.*
import com.usit.hub4tickets.login.LoginBaseInteractor
import com.usit.hub4tickets.login.ui.LoginActivity
import com.usit.hub4tickets.utils.*
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class LoginPresenterImpl(private val mView: LoginPresenter.MainView, context: Context) : LoginPresenter,
    LoginAPICallListener {

    private val mContext = context

    private val loginInteractor: LoginBaseInteractor =
        LoginBaseInteractor(this)

    override fun presentState(state: LoginPresenter.MainView.ViewState) {
        // user state logging
        Log.i(LoginActivity::class.java.simpleName, state.name)
        when (state) {
            IDLE -> mView.showState(IDLE)
            LOADING -> mView.showState(LOADING)
            SUCCESS -> mView.showState(SUCCESS)
            VERIFY_OTP_SUCCESS -> mView.showState(VERIFY_OTP_SUCCESS)
            SEND_OTP_SUCCESS -> mView.showState(SEND_OTP_SUCCESS)
            CHANGE_PASSWORD_SUCCESS -> mView.showState(CHANGE_PASSWORD_SUCCESS)
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

    override fun onAPICallSucceed(route: Enums.APIRoute, responseModel: LoginViewModel.LoginResponse) = when (route) {
        Enums.APIRoute.GET_SAMPLE -> {
            if (responseModel.status.equals("1")) {
                mView.doRetrieveModel()?.loginDomain = responseModel
                Pref.setValue(mContext, PrefConstants.IS_LOGIN, true)
                Pref.setValue(mContext, PrefConstants.USER_ID, responseModel.responseData?.userId.toString())
                Pref.setValue(mContext, PrefConstants.EMAIL_ID, responseModel.responseData?.email.toString())
                presentState(SUCCESS)
            } else {
                mView.doRetrieveModel()?.errorMessage = responseModel.message
                presentState(ERROR)
            }
        }
    }

    override fun onAPICallFailed(route: Enums.APIRoute, message: String?) {
        Utility.hideProgressBar()
        mView.doRetrieveModel()?.errorMessage = message
        presentState(ERROR)
    }

    override fun onSentOtpAPICallSucceed(route: Enums.APIRoute, responseModel: LoginViewModel.LoginResponse) {
        CustomDialogPresenter.showDialog(mContext,
            mContext.resources.getString(R.string.alert_success),
            responseModel.message,
            mContext.resources.getString(
                R.string.ok
            ),
            null,
            object : CustomDialogPresenter.CustomDialogView {
                override fun onPositiveButtonClicked() {
                    presentState(LoginPresenter.MainView.ViewState.SEND_OTP_SUCCESS)
                }

                override fun onNegativeButtonClicked() {
                }
            })
    }

    override fun onVerifyOtpAPICallSucceed(route: Enums.APIRoute, responseModel: LoginViewModel.LoginResponse) {
        if (responseModel.status.equals("1")) {
            mView.doRetrieveModel()?.loginDomain = responseModel
            presentState(VERIFY_OTP_SUCCESS)
        } else {
            mView.doRetrieveModel()?.errorMessage = responseModel.message
            presentState(ERROR)
        }
    }

    override fun onForgotPasswordAPICallSucceed(route: Enums.APIRoute, responseModel: LoginViewModel.LoginResponse) {
        CustomDialogPresenter.showDialog(mContext,
            mContext.resources.getString(R.string.alert_success),
            responseModel.message,
            mContext.resources.getString(
                R.string.ok
            ),
            null,
            object : CustomDialogPresenter.CustomDialogView {
                override fun onPositiveButtonClicked() {
                    presentState(CHANGE_PASSWORD_SUCCESS)
                }

                override fun onNegativeButtonClicked() {
                }
            })
    }

    override fun callAPI(deviceId: String, email: String, password: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            loginInteractor.callAPIGetLogin(
                deviceId,
                email,
                password,
                Constant.Path.DEVICE_FLAG
            )
        } else {
            mView.doRetrieveModel()?.errorMessage =
                    mView.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callSendOtpAPI(deviceId: String, email: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            loginInteractor.callSendOtpAPI(deviceId, email)
        } else {
            mView.doRetrieveModel()?.errorMessage =
                    mView.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callVerifyOTPAPI(deviceId: String, email: String, otp: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            loginInteractor.callAPIVerifyOTP(deviceId, email, otp)
        } else {
            mView.doRetrieveModel()?.errorMessage =
                    mView.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun callResetPassword(deviceId: String, email: String, newPassword: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            loginInteractor.callAPIResetPassword(deviceId, email, newPassword)
        } else {
            mView.doRetrieveModel()?.errorMessage =
                    mView.doRetrieveModel()?.context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }

    }
}
