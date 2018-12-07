package com.usit.hub4tickets.domain.api

import com.usit.hub4tickets.dashboard.model.DashboardViewModel
import com.usit.hub4tickets.utils.Enums

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface DashboardAPICallListener {
    fun onAPICallSucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.SettingsResponse)
    fun onAPICallSaveSucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.SettingsResponse)
    fun onAPICallGetCountrySucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.CountriesResponse)
    fun onAPICallGetStateSucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.StateResponse)
    fun onAPICallGetCitySucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.CityResponse)
    fun onAPICallGetLanguageSucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.LanguageResponse)
    fun onAPICallGetCurrencySucceed(route: Enums.APIRoute, responseModel: DashboardViewModel.CurrencyResponse)
    fun onAPICallFailed(route: Enums.APIRoute, message: String)
}