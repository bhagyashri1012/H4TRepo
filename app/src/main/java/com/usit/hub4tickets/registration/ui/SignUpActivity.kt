package com.usit.hub4tickets.registration.ui

import android.os.Bundle
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 * A signup screen that offers signup via email/password.
 */
class SignUpActivity : BaseActivity() {
    private var signUpFragment: SignUpFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flight)
         title = resources.getString(R.string.action_sign_up)
         mainToolbar.setNavigationOnClickListener { onBackPressed() }
        if (savedInstanceState == null) run { initScreen() }
        else run { signUpFragment = supportFragmentManager.fragments[0] as SignUpFragment }
    }

    private fun initScreen() {
        signUpFragment = SignUpFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container_flight, signUpFragment!!).commit()
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}