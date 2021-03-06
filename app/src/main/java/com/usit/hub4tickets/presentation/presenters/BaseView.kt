package com.usit.hub4tickets.domain.presentation.presenters

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
interface BaseView {

    /**
     * This is a general method used for showing some kind of progress during a background task. For example, this
     * method should show a progress bar and/or disable buttons before some background work starts.
     *
     * @param flag True to show, false to hide progress
     */
    fun showProgress(flag: Boolean)

    /**
     * This method is used for showing toast messages on the UI.
     *
     * @param message
     */
    fun showToast(message: String?)

    /**
     * This method is used for showing messages on the UI via dialog.
     *
     * @param title
     * @param message
     */
    fun showDialog(title: String?, message: String?)
}
