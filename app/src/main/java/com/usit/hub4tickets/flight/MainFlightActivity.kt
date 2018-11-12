package com.usit.hub4tickets.flight

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.DashboardActivity
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
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(baseContext, DashboardActivity::class.java)
                intent.putExtra("SCREEN_NAME", "home")
                startActivity(intent)
                finish()
            }
            R.id.navigation_dashboard -> {
                val intent = Intent(baseContext, DashboardActivity::class.java)
                intent.putExtra("SCREEN_NAME", "account")
                startActivity(intent)
                finish()
            }
            R.id.navigation_help -> {
                val intent = Intent(baseContext, DashboardActivity::class.java)
                intent.putExtra("SCREEN_NAME", "help")
                startActivity(intent)
                finish()
            }
        }

        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flight)
        title = resources.getString(R.string.flight)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, this)
        viewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewPager)
        navigation_flight.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

}
