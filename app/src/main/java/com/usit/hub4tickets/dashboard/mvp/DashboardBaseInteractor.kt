package com.usit.hub4tickets.login

import com.usit.hub4tickets.api.network.ErrorResponse
import com.usit.hub4tickets.domain.api.APICallManager
import com.usit.hub4tickets.domain.api.DashboardAPICallListener
import com.usit.hub4tickets.presentation.presenters.BaseInteractor
import com.usit.hub4tickets.utils.Enums
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class DashboardBaseInteractor(private var listenerSettingsInfo: DashboardAPICallListener) :
    BaseInteractor {
    fun callAPIGetSettingsData(device_id: String, userId: String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getSettingsData(device_id, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error))
            })
    }

    fun callAPISaveSettingsData(
        userId: String,
        deviceId: String,
        countryId: String,
        currencyId: String,
        langId: String
    ) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.saveSettingsData(userId, deviceId,countryId,currencyId,langId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallSaveSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error))
            })
    }


    fun callAPIGetCountry() {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getCountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallGetCountrySucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error))
            })
    }

    fun callAPIGetLanguage() {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getLanguages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallGetLanguageSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error))
            })
    }

    fun callAPIGetCurrency() {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallGetCurrencySucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error))
            })
    }
}