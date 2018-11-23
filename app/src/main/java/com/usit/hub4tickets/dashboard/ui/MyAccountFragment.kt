package com.usit.hub4tickets.dashboard.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.account.ui.AccountInfoFragment
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter.MainView.ViewState.*
import com.usit.hub4tickets.domain.presentation.screens.main.ProfilePresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModel
import com.usit.hub4tickets.login.ui.LoginActivity
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.fragment_myaccount.*
import kotlinx.android.synthetic.main.fragment_myaccount.view.*

class MyAccountFragment : Fragment(), ProfilePresenter.MainView {
    private lateinit var model: ProfileViewModel
    private lateinit var presenter: ProfilePresenter
    private var listener: OnFragmentInteractionListener? = null
    override fun doRetrieveProfileModel(): ProfileViewModel = this.model
    override fun showState(viewState: ProfilePresenter.MainView.ViewState) {
        when (viewState) {
            IDLE -> showProgress(false)
            LOADING -> showProgress(true)
            SUCCESS -> autoFillFields()
            ERROR
            -> {
                showProgress(false)
                Utility.showCustomDialog(
                    context,
                    doRetrieveProfileModel().errorMessage,
                    null
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.link_login.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        view.link_my_alerts.setOnClickListener {
            val intent = Intent(context, AlertsActivity::class.java)
            startActivity(intent)
        }

        view.link_my_history.setOnClickListener {
            val intent = Intent(context, AlertsActivity::class.java)
            startActivity(intent)
        }

        view.link_clear_history.setOnClickListener {
            val intent = Intent(context, AlertsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myaccount, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        if (Pref.getValue(context, PrefConstants.IS_LOGIN, false)) {
            rl_my_acc_info.visibility = View.VISIBLE
            link_login.isClickable = false
            link_login.text = Pref.getValue(context, PrefConstants.EMAIL_ID, "")

        } else {
            link_login.visibility = View.VISIBLE
            link_login.text = "Login"
            link_login.isClickable = true
            rl_my_acc_info.visibility = View.GONE
        }
        link_account_info.setOnClickListener {
            initScreen()
        }

        link_log_out.setOnClickListener { logoutClearData() }
    }

    private fun autoFillFields() {
        tv_country_name.text = model.profileDomain.responseData?.country
        tv_lang_name.text = model.profileDomain.responseData?.language
        tv_currency_name.text = model.profileDomain.responseData?.currency
        Pref.setValue(context, PrefConstants.EMAIL_ID, model.profileDomain.responseData?.email.toString())
        showState(IDLE)
    }

    private fun showProgress(show: Boolean) {
        if (show)
            Utility.showProgressDialog(context = context)
        else
            Utility.hideProgressBar()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    private var accountMainFrag: AccountInfoFragment? = null

    private fun initScreen() {
        accountMainFrag = AccountInfoFragment()
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.replace(R.id.container_account_info, accountMainFrag!!)
            ?.addToBackStack("AccountInfo")?.commit()
    }

    private fun init() {
        this.model = ProfileViewModel(context)
        this.presenter = ProfilePresenterImpl(this, context!!)
        if (null == model.profileDomain.responseData)
            presenter.callAPIGetProfile(Pref.getValue(context, PrefConstants.USER_ID, "0").toString())
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(): MyAccountFragment {
            return MyAccountFragment()
        }
    }

    fun logoutClearData() {
        CustomDialogPresenter.showDialog(
            this!!.context!!,
            context?.resources!!.getString(R.string.alert_log_out),
            getString(R.string.log_out_messege),
            context?.resources!!.getString(
                R.string.ok
            ),
            null,
            object : CustomDialogPresenter.CustomDialogView {
                override fun onPositiveButtonClicked() {
                    Pref.setValue(context, PrefConstants.IS_LOGIN, false)
                    Pref.setValue(context, PrefConstants.USER_ID, "0")
                    Pref.setValue(context, PrefConstants.EMAIL_ID, "")
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }

                override fun onNegativeButtonClicked() {

                }
            })
    }
}
