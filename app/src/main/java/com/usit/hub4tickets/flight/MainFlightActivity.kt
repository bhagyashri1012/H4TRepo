package com.usit.hub4tickets.flight

import android.os.Bundle
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.activity_main_flight.*

/**
 * Created by Bhagyashri Burade
 * Date: 05/11/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class MainFlightActivity : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    private var viewPagerAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flight)
        title = resources.getString(R.string.flight)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager,this)
        viewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
}
