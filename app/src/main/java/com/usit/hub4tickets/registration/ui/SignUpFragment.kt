package com.usit.hub4tickets.registration.ui


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.presenters.SignUpPresenter
import com.usit.hub4tickets.domain.presentation.screens.main.SignUpPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModel
import com.usit.hub4tickets.flight.ui.RootFragment
import com.usit.hub4tickets.login.ui.LoginActivity
import com.usit.hub4tickets.login.ui.LoginFragment
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignUpFragment : RootFragment(), SignUpPresenter.MainView {
    private lateinit var model: SignUpViewModel
    private lateinit var presenter: SignUpPresenter
    override fun doRetrieveModel(): SignUpViewModel = this.model
    override fun showState(viewState: SignUpPresenter.MainView.ViewState) {
        when (viewState) {
            LoginPresenter.MainView.ViewState.IDLE -> Utility.showProgress(false, context)
            LoginPresenter.MainView.ViewState.LOADING -> Utility.showProgress(true, context)
            LoginPresenter.MainView.ViewState.SUCCESS -> showLogin()
            LoginPresenter.MainView.ViewState.ERROR
            -> {
                presenter.presentState(SignUpPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(context, doRetrieveModel().errorMessage, "", null)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        if (Pref.getValue(context, PrefConstants.IS_FIRST_TIME, false)) {
            titleToolBar.text = resources.getString(R.string.action_sign_up)
            mainToolbar.setNavigationOnClickListener { onBackPressed() }
        } else {
            rl_toolbar.visibility = View.GONE
        }
        this.model = SignUpViewModel(context)
        this.presenter = SignUpPresenterImpl(this, context!!)

        edt_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        email_sign_in_button.setOnClickListener { attemptLogin() }
    }

    private var loginFragment: LoginFragment? = null

    private fun showLogin() {
        showState(SignUpPresenter.MainView.ViewState.IDLE)
        if (Pref.getValue(context, PrefConstants.IS_FIRST_TIME, false)) {
            loginFragment = LoginFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.container_account_info, loginFragment!!)
                ?.addToBackStack("LoginFragment")?.commit()
        } else {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }
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
            Utility.showProgress(true, context)
            presenter.callAPI(
                edt_email.text.toString(),
                edt_password.text.toString(),
                check.toString(),
                Pref.getValue(context, PrefConstants.DEFAULT_LOCATION, "")!!,
                Pref.getValue(context, PrefConstants.DEFAULT_LANGUAGE, "")!!
            )
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        var PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})"
        var pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        var matcher: Matcher? = null
        matcher = pattern.matcher(password)
        return matcher!!.matches()
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.popBackStackImmediate()!!
    }
}
