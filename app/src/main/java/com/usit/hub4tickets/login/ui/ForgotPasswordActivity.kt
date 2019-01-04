package com.usit.hub4tickets.login.ui

import android.os.Bundle
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.common_toolbar.*

class ForgotPasswordActivity : BaseActivity() {


    private var forgotPasswordFragment: ForgotPasswordFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flight)
        title = resources.getString(R.string.action_forgot_password)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        if (savedInstanceState == null) run { initScreen() }
        else run { forgotPasswordFragment = supportFragmentManager.fragments[0] as ForgotPasswordFragment }
    }

    private fun initScreen() {
        forgotPasswordFragment = ForgotPasswordFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container_flight, forgotPasswordFragment!!).commit()
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}