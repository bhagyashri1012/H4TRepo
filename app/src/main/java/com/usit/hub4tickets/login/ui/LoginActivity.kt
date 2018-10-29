package com.usit.hub4tickets.login.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.DashboardActivity
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.LoginPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.registration.ui.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), LoginPresenter.MainView {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar;
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var model: LoginViewModel
    private lateinit var presenter: LoginPresenter
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.
        init()
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
        sign_up_button.setOnClickListener { attemptSignUp() }
        forgot_pass_button.setOnClickListener { forgotPassword() }
        tv_skip.setOnClickListener { redirectToDashboard() }
    }

    private fun redirectToDashboard() {
        val intent = Intent(applicationContext, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun forgotPassword() {
        val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun attemptSignUp() {
        val intent = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun init() {
        this.model = LoginViewModel(this)
        this.presenter = LoginPresenterImpl(this, this)
    }

    override fun showState(viewState: LoginPresenter.MainView.ViewState) {
        when (viewState) {
            LoginPresenter.MainView.ViewState.IDLE -> showProgress(false)
            LoginPresenter.MainView.ViewState.LOADING -> showProgress(true)
            LoginPresenter.MainView.ViewState.SHOW_LOGIN_PAGE -> showSignUp()
            LoginPresenter.MainView.ViewState.ERROR -> showSignUp()/* {
                presenter.presentState(LoginPresenter.MainView.ViewState.IDLE)
                showDialog(null, doRetrieveModel().errorMessage)
            }*/
        }
    }

    private fun showSignUp() {
        presenter.presentState(LoginPresenter.MainView.ViewState.IDLE)
        val intent = Intent(applicationContext, SignUpActivity::class.java)
        startActivity(intent)
    }

    override fun doRetrieveModel(): LoginViewModel = this.model

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
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
            // load sample data from API,
            presenter.presentState(LoginPresenter.MainView.ViewState.LOAD_LOGIN)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 2
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    override fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            //login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}
