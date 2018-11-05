package com.usit.hub4tickets.login.ui

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.LoginPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*
import kotlinx.android.synthetic.main.forgot_password_dialog.view.*
import kotlinx.android.synthetic.main.verify_otp_dialog.view.*

class ForgotPasswordActivity : BaseActivity(), LoginPresenter.MainView {

    private lateinit var model: LoginViewModel
    private lateinit var presenter: LoginPresenter
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        title = resources.getString(R.string.action_forgot_password)
        init()
        setClickListeners()
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun showState(viewState: LoginPresenter.MainView.ViewState) {
        when (viewState) {
            LoginPresenter.MainView.ViewState.IDLE -> showProgress(false)
            LoginPresenter.MainView.ViewState.LOADING -> showProgress(true)
            LoginPresenter.MainView.ViewState.VERIFY_OTP_SUCCESS -> forgotPassword()
            LoginPresenter.MainView.ViewState.SEND_OTP_SUCCESS -> verifyOtp()
            LoginPresenter.MainView.ViewState.ERROR -> {
                presenter.presentState(LoginPresenter.MainView.ViewState.IDLE)
                showDialog(null, doRetrieveModel().errorMessage)
            }
        }
    }

    override fun doRetrieveModel(): LoginViewModel = this.model
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

    private fun setClickListeners() {
        send_otp_button.setOnClickListener {
            email = edt_email_forgot_password.text.toString()
            presenter.callForgotPasswordAPI(edt_email_forgot_password.text.toString())
        }

        edt_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptResetPassword()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun attemptResetPassword() {
        // Reset errors.
        edt_current_password.error = null
        edt_password.error = null

        // Store values at the time of the login attempt.
        val currentPasswordStr = edt_current_password.text.toString()
        val passwordStr = edt_password.text.toString()
        val confirmPasswordStr = edt_confirm_password.text.toString()

        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(currentPasswordStr) && !isPasswordValid(currentPasswordStr)) {
            edt_current_password.error = getString(R.string.error_field_required)
            focusView = edt_current_password
            cancel = true
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            edt_password.error = getString(R.string.error_invalid_password)
            focusView = edt_password
            cancel = true
        }
        if (!passwordStr.equals(confirmPasswordStr)) {
            edt_password.error = getString(R.string.error_not_match_password)
            focusView = edt_password
            cancel = false;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            presenter.callResetPassword(
                Utility.getDeviceId(this),
                edt_current_password.text.toString(),
                edt_password.text.toString()
            )
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    private fun verifyOtp() {
        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.verify_otp_dialog, null)
        dialogView.button_verify.setOnClickListener {
            val otp = dialogView.editTextEnterOtp.text.toString()
            presenter.callVerifyOTPAPI(Utility.getDeviceId(this), email, otp)
        }
        dialogView.button_cancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }

    private fun forgotPassword() {
        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.forgot_password_dialog, null)
        dialogView.button_chng_password.setOnClickListener {
            val newPassword = dialogView.edt_password.text.toString()
            presenter.callResetPassword(Utility.getDeviceId(this), email, newPassword)
        }
        dialogView.button_cancel1.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }
}
