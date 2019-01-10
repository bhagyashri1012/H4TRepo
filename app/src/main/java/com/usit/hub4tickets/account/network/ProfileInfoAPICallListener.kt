package com.usit.hub4tickets.domain.api

import com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModel
import com.usit.hub4tickets.utils.Enums

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface ProfileInfoAPICallListener {
    fun onAPICallSucceedSettingData(route: Enums.APIRoute, responseModel: ProfileViewModel.SettingsResponse)
    fun onAPICallSucceed(route: Enums.APIRoute, responseModel: ProfileViewModel.ProfileResponse)
    fun onAPICallUpdatePersonalInfoSucceed(route: Enums.APIRoute, responseModel: ProfileViewModel.ProfileResponse)
    fun onAPICallFailed(route: Enums.APIRoute, message: String)
    fun onAPICallChangePasswordSucceed(
        route: Enums.APIRoute,
        response: ProfileViewModel.ProfileResponse,
        dialogView: android.support.v7.app.AlertDialog
    )

}
