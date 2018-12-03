package com.usit.hub4tickets.flight.ui

import android.os.Bundle
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by Bhagyashri Burade
 * Date: 05/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class MainFlightActivity : BaseActivity() {

    private var flihtMainFrag: FlightMainFragment? = null

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flight)
        title = resources.getString(R.string.flight)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        if (savedInstanceState == null) run { initScreen() }
        else run { flihtMainFrag = supportFragmentManager.fragments[0] as FlightMainFragment }
    }

    private fun initScreen() {
        flihtMainFrag = FlightMainFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container_flight, flihtMainFrag!!).commit()
    }

    override fun onBackPressed() {
        if (!flihtMainFrag!!.onBackPressed()) {
            super.onBackPressed()
        } else {
        }
    }
}