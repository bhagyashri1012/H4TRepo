package com.usit.hub4tickets.dashboard.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.usit.hub4tickets.R
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.activity_dashboard.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class DashboardActivity : AppCompatActivity() {

    private var selectedFragment: Fragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                selectedFragment = HomeFragment.newInstance()
            }
            R.id.navigation_dashboard -> {
                selectedFragment = MyAccountFragment.newInstance()
            }
            R.id.navigation_help -> {
                selectedFragment = HelpFragment.newInstance()
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, selectedFragment!!)
        transaction.commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        if (intent.extras != null) {
            when (intent.extras.get("SCREEN_NAME")) {
                "home" -> loadFragment(HomeFragment.newInstance())
                "account" -> loadFragment(MyAccountFragment.newInstance())
                "help" -> loadFragment(HelpFragment.newInstance())

            }
        } else
            loadFragment(HomeFragment.newInstance())

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


    override fun onBackPressed() {
        if (!Pref.getValue(this, PrefConstants.IS_LOGIN, false)) {
            CustomDialogPresenter.showDialog(
                this!!,
                resources!!.getString(R.string.alert_exit),
                getString(R.string.alert_exit_msg),
                resources!!.getString(
                    R.string.ok
                ),
                getString(R.string.no),
                object : CustomDialogPresenter.CustomDialogView {
                    override fun onPositiveButtonClicked() {
                        finish()
                    }

                    override fun onNegativeButtonClicked() {

                    }
                })
        } else {
            if (navigation.selectedItemId === R.id.navigation_home) {
                super.onBackPressed()
            } else {
                if (supportFragmentManager?.backStackEntryCount == 0) {
                    navigation.selectedItemId = R.id.navigation_home
                } else {
                    supportFragmentManager?.popBackStackImmediate()
                }
            }
        }
    }
}
