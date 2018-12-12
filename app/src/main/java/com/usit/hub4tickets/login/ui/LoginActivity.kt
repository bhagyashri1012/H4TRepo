package com.usit.hub4tickets.login.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.ui.DashboardActivity
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.LoginPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.registration.ui.SignUpActivity
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), LoginPresenter.MainView {

    private lateinit var model: LoginViewModel
    private lateinit var presenter: LoginPresenter
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        setClickListeners()
    }

    override fun doRetrieveModel(): LoginViewModel = this.model

    override fun showState(viewState: LoginPresenter.MainView.ViewState) {
        when (viewState) {
            LoginPresenter.MainView.ViewState.IDLE -> showProgress(false)
            LoginPresenter.MainView.ViewState.LOADING -> showProgress(true)
            LoginPresenter.MainView.ViewState.SUCCESS -> redirectToDashboard()
            LoginPresenter.MainView.ViewState.ERROR -> {
                presenter.presentState(LoginPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(this, doRetrieveModel().errorMessage, "", null)
            }
        }
    }

    override fun showProgress(show: Boolean) {
        if (show)
            Utility.showProgressDialog(context = this)
        else
            Utility.hideProgressBar()
    }

    private fun init() {
        this.model = LoginViewModel(this)
        this.presenter = LoginPresenterImpl(this, this)
    }

    private fun redirectToDashboard() {
        val intent = Intent(applicationContext, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun forgotPassword() {
        val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun attemptSignUp() {
        val intent = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun setClickListeners() {
        edt_password_login.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        sign_in_button.setOnClickListener { attemptLogin() }
        sign_up_button.setOnClickListener { attemptSignUp() }
        forgot_pass_button.setOnClickListener { forgotPassword() }
        tv_skip.setOnClickListener {
            Pref.setValue(this, PrefConstants.IS_LOGIN, false)
            Pref.setValue(this, PrefConstants.IS_FIRST_TIME, true)
            redirectToDashboard()
        }
    }

    private fun attemptLogin() {
        // Reset errors.
        edt_email_login.error = null
        edt_password_login.error = null
        val emailStr = edt_email_login.text.toString()
        val passwordStr = edt_password_login.text.toString()
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(emailStr)) {
            edt_email_login.error = getString(R.string.error_field_required_email)
            focusView = edt_email_login
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            edt_email_login.error = getString(R.string.error_invalid_email)
            focusView = edt_email_login
            cancel = true
        } else if (TextUtils.isEmpty(passwordStr)) {
            edt_password_login.error = getString(R.string.error_field_required_password)
            focusView = edt_password_login
            cancel = true
        } else if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            edt_password_login.error = getString(R.string.error_invalid_password)
            focusView = edt_password_login
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            presenter.callAPI(
                Utility.getDeviceId(this),
                edt_email_login.text.toString(),
                edt_password_login.text.toString()
            )
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 2
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