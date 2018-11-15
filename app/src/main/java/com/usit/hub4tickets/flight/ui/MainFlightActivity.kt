package com.usit.hub4tickets.flight.ui

import android.os.Bundle
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.item_horizontal.*

/**
 * Created by Bhagyashri Burade
 * Date: 05/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class MainFlightActivity : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flight)
        title = resources.getString(R.string.flight)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }

        if (savedInstanceState == null) run {
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserving the fragment stack inside each tab
            initScreen()

        }
        else run {
            // restoring the previously created fragment
            // and getting the reference
            flihtMainFrag = supportFragmentManager.fragments[0] as FlightMainFragment
        }

    }

    private var flihtMainFrag: FlightMainFragment? = null

    private fun initScreen() {
        // Creating the ViewPager container fragment once
        flihtMainFrag = FlightMainFragment()

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.container_flight, flihtMainFrag!!)
            .commit()
    }

    override fun onBackPressed() {
        if (!flihtMainFrag!!.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            super.onBackPressed()

        } else {
            // carousel handled the back pressed task
            // do not call super
        }
    }
}
