package com.usit.hub4tickets.registration.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.presenters.SignUpPresenter
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.SignUpPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModel
import com.usit.hub4tickets.login.ui.LoginActivity
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_signup.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 * A signup screen that offers signup via email/password.
 */
class SignUpActivity : BaseActivity(), SignUpPresenter.MainView {

    private lateinit var model: SignUpViewModel
    private lateinit var presenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        title = resources.getString(R.string.action_sign_up)
        init()
    }

    private fun init() {
        this.model = SignUpViewModel(this)
        this.presenter = SignUpPresenterImpl(this, this)

        edt_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
    }

    override fun doRetrieveModel(): SignUpViewModel = this.model

    override fun showState(viewState: SignUpPresenter.MainView.ViewState) {
        when (viewState) {
            LoginPresenter.MainView.ViewState.IDLE -> showProgress(false)
            LoginPresenter.MainView.ViewState.LOADING -> showProgress(true)
            LoginPresenter.MainView.ViewState.SUCCESS -> showLogin()
            LoginPresenter.MainView.ViewState.ERROR
            -> {
                presenter.presentState(SignUpPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(this, doRetrieveModel().errorMessage,R.string.alert_failure, null)
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun showProgress(show: Boolean) {
        if (show)
            Utility.showProgressDialog(context = this)
        else
            Utility.hideProgressBar()
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        edt_email.error = null
        edt_password.error = null

        // Store values at the time of the login attempt.
        val emailStr = edt_email.text.toString()
        val passwordStr = edt_password.text.toString()
        val confirmPasswordStr = edt_confirm_password_signup.text.toString()

        var cancel = false
        var focusView: View? = null
        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            edt_email.error = getString(R.string.error_field_required)
            focusView = edt_email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            edt_email.error = getString(R.string.error_invalid_email)
            focusView = edt_email
            cancel = true
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            edt_password.error = getString(R.string.error_invalid_password)
            focusView = edt_password
            cancel = true
        }
        if (!passwordStr.equals(confirmPasswordStr)) {
            edt_confirm_password_signup.error = getString(R.string.error_not_match_password)
            focusView = edt_confirm_password_signup
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            presenter.callAPI(edt_email.text.toString(), edt_password.text.toString())
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}
