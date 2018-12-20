package com.usit.hub4tickets.login

import android.support.v7.app.AlertDialog
import com.usit.hub4tickets.api.network.ErrorResponse
import com.usit.hub4tickets.domain.api.APICallManager
import com.usit.hub4tickets.domain.api.LoginAPICallListener
import com.usit.hub4tickets.utils.Enums
import com.usit.hub4tickets.utils.presentation.presenters.BaseInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class LoginBaseInteractor(private var listener: LoginAPICallListener) :
    BaseInteractor {
    fun callAPIGetLogin(device_id: String, email: String, password: String, deviceFlag: Int) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getLogin(device_id, email, password, deviceFlag)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listener.onAPICallSucceed(route, response)
            },
            { error ->
                listener.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callSendOtpAPI(deviceId: String, email: String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.sendOtp(deviceId, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listener.onSentOtpAPICallSucceed(route, response)
            },
            { error ->
                listener.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIVerifyOTP(
        device_id: String,
        email: String,
        otp: String,
        dialogBuilder: AlertDialog
    ) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.verifyOTP(device_id, email, otp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listener.onVerifyOtpAPICallSucceed(route, response,dialogBuilder)
            },
            { error ->
                listener.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIResetPassword(
        device_id: String,
        email: String,
        newPassword: String,
        dialogBuilder: AlertDialog
    ) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.resetPassword(device_id, email, newPassword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listener.onForgotPasswordAPICallSucceed(route, response,dialogBuilder)
            },
            { error ->
                listener.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }
}