package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import android.graphics.Color
import android.provider.Settings.Secure
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.usit.hub4tickets.MainApplication
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.api.SignUpAPICallListener
import com.usit.hub4tickets.domain.presentation.presenters.SignUpPresenter
import com.usit.hub4tickets.domain.presentation.presenters.SignUpPresenter.MainView.ViewState.*
import com.usit.hub4tickets.login.SignUpBaseInteractor
import com.usit.hub4tickets.registration.ui.SignUpActivity
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Enums

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SignUpPresenterImpl(
    private val mView: SignUpPresenter.MainView,
    context: Context
) : SignUpPresenter, SignUpAPICallListener {
    private val signUpBaseInteractor: SignUpBaseInteractor =
        SignUpBaseInteractor(this)
    private val mContext = context
    private var device_id = Secure.getString(mContext.contentResolver, Secure.ANDROID_ID)

    override fun callAPI(email: String, password: String) {
        if (MainApplication.getInstance.isConnected()) {
            presentState(LOADING)
            signUpBaseInteractor.callAPIGetSignUp(
                device_id,
                email,
                password,
                Constant.Path.DEVICE_FLAG
            )
        } else {
            mView.doRetrieveModel().errorMessage =
                    mView.doRetrieveModel().context?.getString(R.string.message_no_internet)
            presentState(ERROR)
        }
    }

    override fun presentState(state: SignUpPresenter.MainView.ViewState) {
        // user state logging
        Log.i(SignUpActivity::class.java.simpleName, state.name)
        when (state) {
            IDLE -> mView.showState(IDLE)
            LOADING -> mView.showState(LOADING)
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

    override fun onAPICallSucceed(route: Enums.APIRoute, responseModel: SignUpViewModel.SignUpResponse) = when (route) {
        Enums.APIRoute.GET_SAMPLE -> {
            mView.doRetrieveModel().signUpDomain = responseModel
            MaterialDialog(mContext)
                .title(text = responseModel.message)
                .positiveButton(R.string.ok) { dialog ->
                    presentState(SUCCESS)
                }
                .show()
        }
    }

    override fun onAPICallFailed(route: Enums.APIRoute, throwable: Throwable) {
        mView.doRetrieveModel().errorMessage = throwable.message
        presentState(ERROR)
    }
}
