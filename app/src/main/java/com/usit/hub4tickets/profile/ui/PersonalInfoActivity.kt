package com.usit.hub4tickets.profile.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter.MainView.ViewState.*
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.ProfilePresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModel
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.common_toolbar.*


class PersonalInfoActivity : BaseActivity(), ProfilePresenter.MainView {
    private lateinit var model: ProfileViewModel
    private lateinit var presenter: ProfilePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
        title = resources.getString(R.string.personal_information)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        init()
        setClickListeners()
        presenter.callAPIGetProfile(Pref.getValue(this, PrefConstants.USER_ID, "").toString())
    }

    private fun setClickListeners() {
        update_button.setOnClickListener { attemptUpdate() }
    }

    private fun init() {
        this.model = ProfileViewModel(this)
        this.presenter = ProfilePresenterImpl(this, this)
    }

    override fun doRetrieveModel(): ProfileViewModel = this.model
    override fun showState(viewState: ProfilePresenter.MainView.ViewState) {
        when (viewState) {
            IDLE -> showProgress(false)
            LOADING -> showProgress(true)
            SUCCESS -> autoFillFields()
            UPDATE_SUCCESS -> onBackPressed()
            ERROR
            -> {
                presenter.presentState(ProfilePresenter.MainView.ViewState.IDLE)
                showDialog(null, doRetrieveModel().errorMessage)
            }
        }
    }

    private fun autoFillFields() {

        edt_email.setText(model.profileDomain.responseData?.email)
        edt_first_name.setText(model.profileDomain.responseData?.firstName)
        edt_last_name.setText(model.profileDomain.responseData?.lastName)
        edt_phone_no.setText(model.profileDomain.responseData?.phoneNumber)
        edt_country.setText(model.profileDomain.responseData?.country)
        edt_state.setText(model.profileDomain.responseData?.state)
        edt_city.setText(model.profileDomain.responseData?.city)
        edt_home_airport.setText(model.profileDomain.responseData?.homeAirPort)
        edt_time_zone.setText(model.profileDomain.responseData?.timeZone)
        edt_lang.setText(model.profileDomain.responseData?.language)
        presenter.presentState(ProfilePresenter.MainView.ViewState.IDLE)

    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptUpdate() {
        /* if (mAuthTask != null) {
             return
         }
 */
        // Reset errors.
        edt_email.error = null
        edt_phone_no.error = null

        // Store values at the time of the login attempt.
        val emailStr = edt_email.text.toString()
        val passwordStr = edt_phone_no.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPhoneNumber(passwordStr)) {
            edt_phone_no.error = getString(R.string.error_field_required)
            focusView = edt_phone_no
            cancel = true
        }

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            presenter.callAPIUpdateProfile(
                Pref.getValue(this, PrefConstants.USER_ID, "").toString(),
                edt_email.text.toString(),
                edt_first_name.text.toString(),
                edt_last_name.text.toString(),
                edt_phone_no.text.toString(),
                edt_home_airport.text.toString(),
                edt_time_zone.text.toString(),
                edt_country.text.toString(),
                edt_state.text.toString(),
                edt_city.text.toString(),
                edt_lang.text.toString()
            )

        }
    }

    override fun showProgress(show: Boolean) {
        if (show)
            Utility.showProgressDialog(context = this)
        else
            Utility.hideProgressBar()
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPhoneNumber(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 8
    }
}
