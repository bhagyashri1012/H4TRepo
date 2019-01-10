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
import com.usit.hub4tickets.utils.Utility.showProgress
import kotlinx.android.synthetic.main.change_password_dialog.view.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_account_info.*

class AccountInfoFragment : RootFragment(), ProfilePresenter.MainView {
    private lateinit var model: ProfileViewModel
    private lateinit var presenter: ProfilePresenter
    override fun doRetrieveProfileModel(): ProfileViewModel = this.model
    override fun showState(viewState: ProfilePresenter.MainView.ViewState) {
        when (viewState) {
            ProfilePresenter.MainView.ViewState.IDLE -> showProgress(false, context)
            ProfilePresenter.MainView.ViewState.LOADING -> showProgress(true, context)
            ProfilePresenter.MainView.ViewState.SUCCESS -> showProgress(false, context)
            ProfilePresenter.MainView.ViewState.CHANGE_PASSWORD_SUCCESS -> showProgress(false, context)
            ProfilePresenter.MainView.ViewState.ERROR
            -> {
                presenter.presentState(ProfilePresenter.MainView.ViewState.IDLE)
                showProgress(false, context)
                Utility.showCustomDialog(
                    context,
                    doRetrieveProfileModel().errorMessage,
                    "",
                    null
                )
            }
        }
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
        titleToolBar.text = resources.getString(R.string.my_account_info)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
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
        dialogView.edt_change_current_password.error = null
        dialogView.edt_new_change_password.error = null
        dialogView.edt_change_confirm_password.error = null
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
        if (!TextUtils.isEmpty(olPassStr) && !Utility.isPasswordValid(olPassStr)) {
            dialogView.edt_change_current_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_change_current_password
            cancel = true
        }
        if (!TextUtils.isEmpty(newPasswordStr) && !Utility.isPasswordValid(newPasswordStr)) {
            dialogView.edt_new_change_password.error = getString(R.string.error_invalid_password)
            focusView = dialogView.edt_new_change_password
            cancel = true
        }
        if (!TextUtils.isEmpty(confirmPasswordStr) && !Utility.isPasswordValid(confirmPasswordStr)) {
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
            focusView?.requestFocus()
        } else {
            showProgress(true, context)
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

    override fun onBackPressed(): Boolean {
        return fragmentManager?.popBackStackImmediate()!!
    }
}
