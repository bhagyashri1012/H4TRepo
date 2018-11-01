package com.usit.hub4tickets.login.ui

import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.LoginPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_forgot_password.*
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
            LoginPresenter.MainView.ViewState.VERIFY_OTP_SUCCESS -> {
                showDialog(null, doRetrieveModel().loginDomain.message)
            }
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
    }

    private fun verifyOtp() {
        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.verify_otp_dialog, null)

        dialogView.button_verify.setOnClickListener {
            val otp = dialogView.editTextEnterOtp.text.toString()
            presenter.callVerifyOTPAPI(email, otp)
        }
        dialogView.button_cancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }
}
