package com.usit.hub4tickets.login

import com.usit.hub4tickets.api.network.ErrorResponse
import com.usit.hub4tickets.domain.api.SignUpAPICallListener
import com.usit.hub4tickets.domain.api.APICallManager
import com.usit.hub4tickets.presentation.presenters.BaseInteractor
import com.usit.hub4tickets.utils.Enums
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SignUpBaseInteractor(private var listenerSignUp: SignUpAPICallListener) :
    BaseInteractor {
    fun callAPIGetSignUp(device_id: String, email: String, password: String, deviceFlag: Int) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getSignUp(device_id, email, password, deviceFlag)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listenerSignUp.onAPICallSucceed(route, response)
            },
            { error ->
                listenerSignUp.onAPICallFailed(route, ErrorResponse.parseError(error))
            })
    }
}