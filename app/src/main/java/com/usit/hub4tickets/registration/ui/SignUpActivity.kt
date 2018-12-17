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
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 * A signup screen that offers signup via email/password.
 */
class SignUpActivity : BaseActivity(), SignUpPresenter.MainView {

    private lateinit var model: SignUpViewModel
    private lateinit var presenter: SignUpPresenter
    override fun doRetrieveModel(): SignUpViewModel = this.model
    override fun showState(viewState: SignUpPresenter.MainView.ViewState) {
        when (viewState) {
            LoginPresenter.MainView.ViewState.IDLE -> showProgress(false)
            LoginPresenter.MainView.ViewState.LOADING -> showProgress(true)
            LoginPresenter.MainView.ViewState.SUCCESS -> showLogin()
            LoginPresenter.MainView.ViewState.ERROR
            -> {
                presenter.presentState(SignUpPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(this, doRetrieveModel().errorMessage, "", null)
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        title = resources.getString(R.string.action_sign_up)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
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

    private fun attemptLogin() {
        // Reset errors.
        edt_email.error = null
        edt_password.error = null
        val emailStr = edt_email.text.toString()
        val passwordStr = edt_password.text.toString()
        val confirmPasswordStr = edt_confirm_password_signup.text.toString()
        var check = if (!checkbox_promotion.isChecked) "0" else "1"
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(emailStr)) {
            edt_email.error = getString(R.string.error_field_required_email)
            focusView = edt_email
            cancel = true
        } else if (!Utility.isEmailValid(emailStr)) {
            edt_email.error = getString(R.string.error_invalid_email)
            focusView = edt_email
            cancel = true
        } else if (TextUtils.isEmpty(passwordStr)) {
            edt_password.error = getString(R.string.error_field_required_password)
            focusView = edt_password
            cancel = true
        } else if (TextUtils.isEmpty(confirmPasswordStr)) {
            edt_confirm_password_signup.error = getString(R.string.error_field_required_re_password)
            focusView = edt_confirm_password_signup
            cancel = true
        } else if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            edt_password.error = getString(R.string.error_invalid_password)
            focusView = edt_password
            cancel = true
        } else if (!passwordStr.equals(confirmPasswordStr)) {
            edt_confirm_password_signup.error = getString(R.string.error_not_match_password)
            focusView = edt_confirm_password_signup
            cancel = true
        }
        checkbox_promotion.setOnCheckedChangeListener { buttonView, isChecked ->
            check = if (isChecked) "0" else "1"
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            presenter.callAPI(edt_email.text.toString(), edt_password.text.toString(), check.toString())
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}