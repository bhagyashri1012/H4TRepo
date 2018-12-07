package com.usit.hub4tickets.login

import com.usit.hub4tickets.api.network.ErrorResponse
import com.usit.hub4tickets.domain.api.APICallManager
import com.usit.hub4tickets.domain.api.ProfileInfoAPICallListener
import com.usit.hub4tickets.utils.presentation.presenters.BaseInteractor
import com.usit.hub4tickets.utils.Enums
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class ProfileBaseInteractor(private var listenerProfileInfo: ProfileInfoAPICallListener) :
    BaseInteractor {
    fun callAPIGetPersonalInfo(device_id: String, userId: String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getProfileData(device_id, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerProfileInfo.onAPICallSucceed(route, response)
            },
            { error ->
                listenerProfileInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIUpdatePersonalInfo(
        userId: String,
        device_id: String,
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
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.updateProfileData(
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerProfileInfo.onAPICallUpdatePersonalInfoSucceed(route, response)
            },
            { error ->
                listenerProfileInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIResetPassword(
        dialogView: android.support.v7.app.AlertDialog,
        userId: String,
        device_id: String,
        oldPassword: String,
        newPassword: String
    ) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.changePassword(userId, device_id, oldPassword, newPassword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerProfileInfo.onAPICallChangePasswordSucceed(route, response, dialogView)
            },
            { error ->
                listenerProfileInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }
}