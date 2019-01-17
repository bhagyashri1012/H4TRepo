package com.usit.hub4tickets.login

import com.usit.hub4tickets.api.network.ErrorResponse
import com.usit.hub4tickets.domain.api.APICallManager
import com.usit.hub4tickets.domain.api.DashboardAPICallListener
import com.usit.hub4tickets.utils.Enums
import com.usit.hub4tickets.utils.presentation.presenters.BaseInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class DashboardBaseInteractor(private var listenerSettingsInfo: DashboardAPICallListener) :
    BaseInteractor {
    fun callAPIGetSettingsData(device_id: String, userId: String, location: String, lang: String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getSettingsData(device_id, userId, location, lang)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
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
        val call =
            APICallManager.getInstance.apiManager.saveSettingsData(userId, deviceId, countryId, currencyId, langId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallSaveSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
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
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIGetState(countryId: String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getStates(countryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallGetStateSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIGetCity(stateId: String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getCities(stateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallGetCitySucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
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
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIGetTimeZone() {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getTimeZones()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallGetTimeZoneSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPIGetAirports() {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getAirports()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallGetAirportsSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
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
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }

    fun callAPISetLocation(userId: String, deviceId: String, locationCode: String, langId: String) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.setLocation(userId, deviceId, locationCode, langId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSettingsInfo.onAPICallSucceed(route, response)
            },
            { error ->
                listenerSettingsInfo.onAPICallFailed(route, ErrorResponse.parseError(error)!!)
            })
    }
}