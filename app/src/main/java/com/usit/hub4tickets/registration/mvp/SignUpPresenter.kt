package com.usit.hub4tickets.domain.presentation.presenters

import com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModel

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface SignUpPresenter : BasePresenter {
    interface MainView {
        /**
         * This enum is used for determine the current state of this screen
         */
        enum class ViewState {
            IDLE, LOADING, LOAD_SIGN_UP, SUCCESS, ERROR,
        }

        /**
         * This method is to show the current state of this screen
         *
         * @param viewState
         */
        fun showState(viewState: ViewState)

        /**
         * This function return the model that was belong to this screen
         *
         * @return
         */
        fun doRetrieveModel(): SignUpViewModel

    }

    /**
     * This method is used for present the current state of this screen
     *
     * @param state
     */
    fun presentState(state: MainView.ViewState)

    fun callAPI(signUp: String, toString: String)
}
