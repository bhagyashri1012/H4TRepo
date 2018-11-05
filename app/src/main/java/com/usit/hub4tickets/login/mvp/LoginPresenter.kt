package com.usit.hub4tickets.domain.presentation.presenters

import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface LoginPresenter : BasePresenter {
    interface MainView {
        /**
         * This enum is used for determine the current state of this screen
         */
        enum class ViewState {
            IDLE, LOADING, VERIFY_OTP_SUCCESS, SUCCESS, ERROR, SEND_OTP_SUCCESS
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
        fun doRetrieveModel(): LoginViewModel
    }

    /**
     * This method is used for present the current state of this screen
     *
     * @param state
     */
    fun presentState(state: MainView.ViewState)

    fun callAPI(deviceId: String,email: String, password: String)
    fun callForgotPasswordAPI(toString: String)
    fun callVerifyOTPAPI(deviceId: String,email: String, otp: String)
    fun callResetPassword(deviceId: String, email: String, newPassword: String)
}
