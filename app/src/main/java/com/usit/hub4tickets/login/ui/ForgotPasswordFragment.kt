package com.usit.hub4tickets.login.ui


import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.screens.main.LoginPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.flight.ui.RootFragment
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.forgot_password_dialog.view.*
import kotlinx.android.synthetic.main.verify_otp_dialog.view.*

class ForgotPasswordFragment : RootFragment(), LoginPresenter.MainView {
    private var model: LoginViewModel? = null
    private var presenter: LoginPresenter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setClickListeners()
    }

    override fun showState(
        viewState: LoginPresenter.MainView.ViewState
    ) {
        when (viewState) {
            LoginPresenter.MainView.ViewState.IDLE -> Utility.showProgress(false, context)
            LoginPresenter.MainView.ViewState.LOADING -> Utility.showProgress(true, context)
            LoginPresenter.MainView.ViewState.VERIFY_OTP_SUCCESS ->
                forgotPassword()
            LoginPresenter.MainView.ViewState.SEND_OTP_SUCCESS ->
                verifyOtp()
            LoginPresenter.MainView.ViewState.CHANGE_PASSWORD_SUCCESS -> redirectToLogin()
            LoginPresenter.MainView.ViewState.ERROR -> {
                presenter?.presentState(LoginPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(context, doRetrieveModel()?.errorMessage, "", null)
            }
        }
    }

    override fun doRetrieveModel(): LoginViewModel? = this.model

    private fun init() {
        if (Pref.getValue(context, PrefConstants.IS_FIRST_TIME, false)) {
            titleToolBar.text = resources.getString(R.string.action_forgot_password)
            mainToolbar.setNavigationOnClickListener { onBackPressed() }
        } else {
            rl_toolbar.visibility = View.GONE
        }
        this.model = LoginViewModel(context)
        this.presenter = LoginPresenterImpl(this, context!!)
    }

    private fun setClickListeners() {
        send_otp_button.setOnClickListener {
            attemptSendOtp()
        }
    }

    private fun attemptResetPassword(dialogView: View, dialogBuilder: AlertDialog) {
        dialogView.edt_new_password.error = null
        dialogView.edt_confirm_password.error = null
        val passwordStr = dialogView.edt_new_password.text.toString()
        val confirmPasswordStr = dialogView.edt_confirm_password.text.toString()
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(passwordStr)) {
            dialogView.edt_new_password.error = getString(R.string.error_field_required_password)
            focusView = dialogView.edt_new_password
            cancel = true
        }
        if (TextUtils.isEmpty(confirmPasswordStr)) {
            dialogView.edt_confirm_password.error = getString(R.string.error_field_required_re_password)
            focusView = dialogView.edt_confirm_password
            cancel = true
        }
        if (!TextUtils.isEmpty(passwordStr) && !Utility.isPasswordValid(passwordStr)) {
            dialogView.edt_new_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_new_password
            cancel = true
        }
        if (!TextUtils.isEmpty(confirmPasswordStr) && !Utility.isPasswordValid(passwordStr)) {
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
            presenter?.callResetPassword(
                Utility.getDeviceId(context),
                email,
                dialogView.edt_confirm_password.text.toString(), dialogBuilder
            )
        }
    }

    private fun verifyOtp() {
        val dialogBuilder = AlertDialog.Builder(context!!).create()
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
                    ViewCompat.setBackgroundTintList(
                        dialogView.button_verify,
                        resources.getColorStateList(R.color.colorPrimary)
                    )
                } else {
                    dialogView.button_verify.isClickable = false
                    ViewCompat.setBackgroundTintList(
                        dialogView.button_verify,
                        resources.getColorStateList(R.color.cool_grey)
                    )
                }
            }
        })
        dialogView.button_verify.setOnClickListener {
            val otp = dialogView.editTextEnterOtp.text.toString()
            Utility.hideSoftKeyboard(dialogView.editTextEnterOtp)
            presenter?.callVerifyOTPAPI(Utility.getDeviceId(context), email, otp, dialogBuilder)

        }
        dialogView.button_cancel.setOnClickListener {
            Utility.hideProgressBar()
            Utility.hideSoftKeyboard(dialogView.editTextEnterOtp)
            dialogBuilder.dismiss()
        }
        dialogView.resendOtp.setOnClickListener {
            dialogBuilder.dismiss()
            presenter?.callSendOtpAPI(Utility.getDeviceId(context), email)
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }

    private fun forgotPassword() {
        val dialogBuilder = AlertDialog.Builder(context!!).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.forgot_password_dialog, null)
        dialogView.button_reset_password.setOnClickListener {
            Utility.hideSoftKeyboard(dialogView)
            attemptResetPassword(dialogView, dialogBuilder)
        }
        dialogView.button_cancel_fp.setOnClickListener {
            Utility.hideProgressBar()
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private fun redirectToLogin() {
        onBackPressed()
    }

    var email: String = ""
    private fun attemptSendOtp() {
        edt_email_forgot_password.error = null
        val emailStr = edt_email_forgot_password.text.toString()
        email = emailStr
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(emailStr)) {
            edt_email_forgot_password.error = getString(R.string.error_field_required_email)
            focusView = edt_email_forgot_password
            cancel = true
        }else
        if (!TextUtils.isEmpty(emailStr) && !Utility.isEmailValid(emailStr)) {
            edt_email_forgot_password.error = getString(R.string.error_invalid_email)
            focusView = edt_email_forgot_password
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            presenter?.callSendOtpAPI(Utility.getDeviceId(context), edt_email_forgot_password.text.toString())
        }
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.popBackStackImmediate()!!
    }
}
