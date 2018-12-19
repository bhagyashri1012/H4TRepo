package com.usit.hub4tickets.login.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.ui.DashboardActivity
import com.usit.hub4tickets.dashboard.ui.MyAccountFragment
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter
import com.usit.hub4tickets.domain.presentation.presenters.LoginPresenter.MainView.ViewState.*
import com.usit.hub4tickets.domain.presentation.screens.main.LoginPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModel
import com.usit.hub4tickets.flight.ui.RootFragment
import com.usit.hub4tickets.registration.ui.SignUpActivity
import com.usit.hub4tickets.registration.ui.SignUpFragment
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : RootFragment(), LoginPresenter.MainView {
    private lateinit var model: LoginViewModel
    private lateinit var presenter: LoginPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
    }

    override fun doRetrieveModel(): LoginViewModel = this.model

    override fun showState(
        viewState: LoginPresenter.MainView.ViewState
    ) {
        when (viewState) {
            IDLE -> showProgress(false)
            LOADING -> showProgress(true)
            SUCCESS -> redirectToDashboard()
            ERROR -> {
                presenter.presentState(IDLE)
                Utility.showCustomDialog(context, doRetrieveModel().errorMessage, "", null)
            }
        }
    }

    fun showProgress(show: Boolean) {
        if (show)
            Utility.showProgressDialog(context)
        else
            Utility.hideProgressBar()
    }

    private fun init() {
        this.model = LoginViewModel(context)
        this.presenter = LoginPresenterImpl(this, context!!)
    }

    private fun redirectToDashboard() {
        if (Pref.getValue(context, PrefConstants.IS_FIRST_TIME, false)) {
            showState(IDLE)
            activity?.onBackPressed()

        } else {
            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private var forgotPasswordFragment: ForgotPasswordFragment? = null
    private var signUpFragment: SignUpFragment? = null
    private var myAccountFragment: MyAccountFragment? = null

    private fun forgotPassword() {
        if (Pref.getValue(context, PrefConstants.IS_FIRST_TIME, false)) {
            forgotPasswordFragment = ForgotPasswordFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.container_account_info, forgotPasswordFragment!!)
                ?.addToBackStack("ForgotPasswordFragment")?.commit()
        } else {
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun attemptSignUp() {
        if (Pref.getValue(context, PrefConstants.IS_FIRST_TIME, false)) {
            signUpFragment = SignUpFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.container_account_info, signUpFragment!!)
                ?.addToBackStack("SignUpFragment")?.commit()
        } else {
            val intent = Intent(context, SignUpActivity::class.java)
            startActivity(intent)
        }
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
        if (Pref.getValue(context, PrefConstants.IS_FIRST_TIME, false))
            tv_skip.visibility = View.GONE
        else
            tv_skip.visibility = View.VISIBLE
        tv_skip.setOnClickListener {
            Pref.setValue(context, PrefConstants.IS_LOGIN, false)
            Pref.setValue(context, PrefConstants.IS_FIRST_TIME, true)
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
        } else if (!Utility.isEmailValid(emailStr)) {
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
            presenter.callAPI(
                Utility.getDeviceId(context),
                edt_email_login.text.toString(),
                edt_password_login.text.toString()
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

    inner class PasswordValidator {
        private val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"
        private val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        private var matcher: Matcher? = null

        /**
         * Validate password with regular expression
         * @param password password for validation
         * @return true valid password, false invalid password
         */
        fun validate(password: String): Boolean {

            matcher = pattern.matcher(password)
            return matcher!!.matches()

        }

    }

}
