package com.usit.hub4tickets.login

import com.usit.hub4tickets.domain.api.APICallListener
import com.usit.hub4tickets.domain.api.APICallManager
import com.usit.hub4tickets.domain.interactor.Interactor
import com.usit.hub4tickets.utils.Enums
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SignUpInteractor(private var listener: APICallListener) : Interactor {
    fun callAPIGetLogin(device_id: String, email: String, password: String, deviceFlag: Int) {
        val route = Enums.APIRoute.GET_SAMPLE
        val call = APICallManager.getInstance.apiManager.getLogin(device_id,email,password,deviceFlag)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        call.subscribe(
            { response ->
                listener.onAPICallSucceed(route, response)
            },
            { error ->
                listener.onAPICallFailed(route, error)
            })
    }
}