package com.usit.hub4tickets.account.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter
import com.usit.hub4tickets.domain.presentation.screens.main.ProfilePresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModel
import com.usit.hub4tickets.flight.ui.RootFragment
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.change_password_dialog.view.*
import kotlinx.android.synthetic.main.fragment_account_info.*

class AccountInfoFragment : RootFragment(), ProfilePresenter.MainView {

    private lateinit var model: ProfileViewModel
    private lateinit var presenter: ProfilePresenter
    override fun doRetrieveProfileModel(): ProfileViewModel = this.model
    override fun showState(viewState: ProfilePresenter.MainView.ViewState) {
        when (viewState) {
            ProfilePresenter.MainView.ViewState.IDLE -> showProgress(false)
            ProfilePresenter.MainView.ViewState.LOADING -> showProgress(true)
            ProfilePresenter.MainView.ViewState.SUCCESS -> showProgress(false)
            ProfilePresenter.MainView.ViewState.CHANGE_PASSWORD_SUCCESS -> showProgress(false)
            ProfilePresenter.MainView.ViewState.ERROR
            -> {
                presenter.presentState(ProfilePresenter.MainView.ViewState.IDLE)
                showProgress(false)
                Utility.showCustomDialog(
                    context,
                    doRetrieveProfileModel().errorMessage,
                    "",
                    null
                )
            }
        }
    }

    private fun showProgress(show: Boolean) {
        if (show)
            Utility.showProgressDialog(context = context)
        else
            Utility.hideProgressBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        cv_personal_info.setOnClickListener {
            if (Pref.getValue(context, PrefConstants.IS_LOGIN, false))
                redirectToPersonalInfo()
        }

        cv_change_password.setOnClickListener {
            Utility.hideSoftKeyboard(it)
            changePassword()

        }
    }

    private fun init() {
        this.model = ProfileViewModel(context)
        this.presenter = ProfilePresenterImpl(this, context!!)
    }

    private fun changePassword() {
        val dialogBuilder = AlertDialog.Builder(context!!).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.change_password_dialog, null)
        dialogView.button_chng_password.setOnClickListener {
            attemptChangePassword(dialogView, dialogBuilder)
        }
        dialogView.button_cancel1.setOnClickListener {
            Utility.hideProgressBar()
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }

    private fun redirectToPersonalInfo() {
        val intent = Intent(context, PersonalInfoActivity::class.java)
        startActivity(intent)
    }

    private fun attemptChangePassword(
        dialogView: View,
        dialogBuilder: AlertDialog
    ) {
        // Reset errors.
        dialogView.edt_change_current_password.error = null
        dialogView.edt_new_change_password.error = null
        dialogView.edt_change_confirm_password.error = null

        // Store values at the time of the login attempt.
        val olPassStr = dialogView.edt_change_current_password.text.toString()
        val newPasswordStr = dialogView.edt_new_change_password.text.toString()
        val confirmPasswordStr = dialogView.edt_change_confirm_password.text.toString()

        var cancel = false
        var focusView: View? = null
        // Check for a valid email address.
        if (TextUtils.isEmpty(olPassStr)) {
            dialogView.edt_change_current_password.error = getString(R.string.error_field_required_password)
            focusView = dialogView.edt_change_current_password
            cancel = true
        } else if (TextUtils.isEmpty(newPasswordStr)) {
            dialogView.edt_new_change_password.error = getString(R.string.error_field_required_password)
            focusView = dialogView.edt_new_change_password
            cancel = true
        } else if (TextUtils.isEmpty(confirmPasswordStr)) {
            dialogView.edt_change_confirm_password.error = getString(R.string.error_field_required_re_password)
            focusView = dialogView.edt_change_confirm_password
            cancel = true
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(olPassStr) && !isPasswordValid(olPassStr)) {
            dialogView.edt_change_current_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_change_current_password
            cancel = true
        }
        if (!TextUtils.isEmpty(newPasswordStr) && !isPasswordValid(newPasswordStr)) {
            dialogView.edt_new_change_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_new_change_password
            cancel = true
        }
        if (!TextUtils.isEmpty(confirmPasswordStr) && !isPasswordValid(confirmPasswordStr)) {
            dialogView.edt_change_confirm_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_change_confirm_password
            cancel = true
        }
        if (!newPasswordStr.equals(confirmPasswordStr)) {
            dialogView.edt_change_confirm_password.error = getString(R.string.error_not_match_password)
            focusView = dialogView.edt_change_confirm_password
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
            presenter.callAPIChangePassword(
                dialogBuilder,
                Pref.getValue(context, PrefConstants.USER_ID, "0").toString()
                ,
                Utility.getDeviceId(context!!),
                dialogView.edt_change_current_password.text.toString(),
                dialogView.edt_change_confirm_password.text.toString()
            )
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountInfoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onBackPressed(): Boolean {
        return this?.onBackPressed()
    }
}
