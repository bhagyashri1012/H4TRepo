package com.usit.hub4tickets.flight.ui

import android.support.v4.app.DialogFragment


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
open class RootDialogFragment : DialogFragment(), OnBackPressListener {

    override fun onBackPressed(): Boolean {
        return BackPressImpl(this).onBackPressed()
    }
}
