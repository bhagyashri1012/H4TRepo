package com.usit.hub4tickets.flight.ui

import android.support.v4.app.Fragment

/**
 * Created by shahabuddin on 6/6/14.
 */
open class RootFragment : Fragment(), OnBackPressListener {

    override fun onBackPressed(): Boolean {
        return BackPressImpl(this).onBackPressed()
    }
}
