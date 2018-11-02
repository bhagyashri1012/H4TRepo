package com.usit.hub4tickets.dashboard.model

import android.content.Context


/**
 * Created by Bhagyashri Burade
 * Date: 02/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class DashboardViewModel(var context: Context?) {
    var errorMessage: String? = null
    var dashboradCountriesDomain: DashboardViewModel.CountriesResponse =
        DashboardViewModel.CountriesResponse(countryData = null, message = null, status = null)

    var dashboradLangDomain: DashboardViewModel.LanguageResponse =
        DashboardViewModel.LanguageResponse(languageData = null, message = null, status = null)

    data class CountriesResponse(
        val countryData: List<CountryData>?,
        val message: String?,
        val status: Int?
    )

    data class CountryData(
        val countryCode: String,
        val countryId: String,
        val countryName: String
    )

    data class LanguageResponse(
        val languageData: List<LanguageData>?,
        val message: String?,
        val status: Int?
    )

    data class LanguageData(
        val languageCode: String,
        val languageId: String,
        val languageName: String
    )
}