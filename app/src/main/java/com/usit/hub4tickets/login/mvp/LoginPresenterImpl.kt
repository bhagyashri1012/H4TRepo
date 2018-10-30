package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import android.provider.Settings.Secure
import android.util.Log
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.api.LoginAPICallListener
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter.MainView.ViewState.*
import com.usit.hub4tickets.login.LoginBaseInteractor
import com.usit.hub4tickets.login.ui.LoginActivity
import com.usit.hub4tickets.utils.Enums


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class LoginPresenterImpl(private val mView: LoginPresenter.MainView, context: Context) : LoginPresenter,
    LoginAPICallListener {
    private val loginInteractor: LoginBaseInteractor =
        LoginBaseInteractor(this)
    private val mContext = context
    private var device_id = Secure.getString(mContext.contentResolver, Secure.ANDROID_ID)
    override fun presentState(state: LoginPresenter.MainView.ViewState) {
        // user state logging
        Log.i(LoginActivity::class.java.simpleName, state.name)
        when (state) {
            IDLE -> mView.showState(IDLE)
            LOADING -> mView.showState(LOADING)
            LOAD_LOGIN ->
                if (MainApplication.getInstance.isConnected()) {
                    presentState(LOADING)
                    loginInteractor.callAPIGetLogin(
                        "0e83ff56a12a9cf0c7290cbb08ab6752181fb54b",
                        "sanjay0707@yopmail.com",
                        "123",
                        1
                    )
                } else {
                    mView.doRetrieveModel().errorMessage =
                            mView.doRetrieveModel().context?.getString(R.string.message_no_internet)
                    presentState(ERROR)
                }
            SUCCESS -> mView.showState(SUCCESS)
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
            mView.doRetrieveModel().loginDomain = responseModel
            presentState(SUCCESS)
        }
    }

    override fun onAPICallFailed(route: Enums.APIRoute, throwable: Throwable) {
        mView.doRetrieveModel().errorMessage = throwable.message
        presentState(ERROR)
    }
}
