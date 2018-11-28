package com.usit.hub4tickets.login.ui

import android.os.Bundle
import android.support.v4.view.ViewCompat.setBackgroundTintList
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.LoginPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_forgot_password.*
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
            LoginPresenter.MainView.ViewState.VERIFY_OTP_SUCCESS ->
                forgotPassword()
            LoginPresenter.MainView.ViewState.SEND_OTP_SUCCESS ->
                verifyOtp()
            LoginPresenter.MainView.ViewState.CHANGE_PASSWORD_SUCCESS -> redirectToLogin()
            LoginPresenter.MainView.ViewState.ERROR -> {
                presenter.presentState(LoginPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(this, doRetrieveModel().errorMessage,R.string.alert_failure, null)
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
            presenter.callSendOtpAPI(Utility.getDeviceId(this), edt_email_forgot_password.text.toString())
        }
    }

    private fun attemptResetPassword(dialogView: View) {
        dialogView.edt_new_password.error = null
        dialogView.edt_confirm_password.error = null
        val passwordStr = dialogView.edt_new_password.text.toString()
        val confirmPasswordStr = dialogView.edt_confirm_password.text.toString()
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(passwordStr)) {
            dialogView.edt_new_password.error = getString(R.string.error_field_required)
            focusView = dialogView.edt_new_password
            cancel = true
        } else if (TextUtils.isEmpty(confirmPasswordStr)) {
            dialogView.edt_confirm_password.error = getString(R.string.error_field_required)
            focusView = dialogView.edt_confirm_password
            cancel = true
        }
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            dialogView.edt_new_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_new_password
            cancel = true
        }
        if (!TextUtils.isEmpty(confirmPasswordStr) && !isPasswordValid(passwordStr)) {
            dialogView.edt_confirm_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_new_password
            cancel = true
        }
        if (!passwordStr.equals(confirmPasswordStr)) {
            dialogView.edt_confirm_password.error = getString(R.string.error_not_match_password)
            focusView = dialogView.edt_confirm_password
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            presenter.callResetPassword(
                Utility.getDeviceId(this),
                email,
                dialogView.edt_confirm_password.text.toString()
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
        dialogView.editTextEnterOtp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 4) {
                    dialogView.button_verify.isClickable = true
                    setBackgroundTintList(dialogView.button_verify, resources.getColorStateList(R.color.colorPrimary))
                } else {
                    dialogView.button_verify.isClickable = false
                    setBackgroundTintList(dialogView.button_verify, resources.getColorStateList(R.color.cool_grey))
                }
            }
        })
        dialogView.button_verify.setOnClickListener {
            val otp = dialogView.editTextEnterOtp.text.toString()
            Utility.hideSoftKeyboard(dialogView.editTextEnterOtp)
            presenter.callVerifyOTPAPI(Utility.getDeviceId(this), email, otp)
        }
        dialogView.button_cancel.setOnClickListener {
            Utility.hideProgressBar()
            Utility.hideSoftKeyboard(dialogView.editTextEnterOtp)
            dialogBuilder.dismiss()
        }
        dialogView.resendOtp.setOnClickListener {
            presenter.callSendOtpAPI(Utility.getDeviceId(this), email)
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }

    private fun forgotPassword() {
        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.forgot_password_dialog, null)
        dialogView.button_reset_password.setOnClickListener {
            Utility.hideKeyBordActivity(this)
            attemptResetPassword(dialogView)
        }
        dialogView.button_cancel_fp.setOnClickListener {
            Utility.hideProgressBar()
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }

    private fun redirectToLogin() {
        onBackPressed()
    }
}