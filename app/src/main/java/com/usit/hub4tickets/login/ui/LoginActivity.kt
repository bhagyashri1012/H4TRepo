package com.usit.hub4tickets.login.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    private var loginFrag: LoginFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flight)
        rl_toolbar.visibility= View.GONE
        if (savedInstanceState == null) run { initScreen() }
        else run { loginFrag = supportFragmentManager.fragments[0] as LoginFragment }
    }

    private fun initScreen() {
        loginFrag = LoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container_flight, loginFrag!!).commit()
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
            super.onBackPressed()
        }
    }
}