package com.usit.hub4tickets.dashboard.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.account.ui.AccountInfoFragment
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter.MainView.ViewState.*
import com.usit.hub4tickets.domain.presentation.screens.main.ProfilePresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModel
import com.usit.hub4tickets.flight.ui.RootFragment
import com.usit.hub4tickets.login.ui.LoginFragment
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_myaccount.*
import kotlinx.android.synthetic.main.fragment_myaccount.view.*

class MyAccountFragment : RootFragment(), ProfilePresenter.MainView {
    private lateinit var model: ProfileViewModel
    private lateinit var presenter: ProfilePresenter
    private var listener: OnFragmentInteractionListener? = null
    override fun doRetrieveProfileModel(): ProfileViewModel = this.model
    override fun showState(viewState: ProfilePresenter.MainView.ViewState) {
        when (viewState) {
            IDLE -> Utility.showProgress(false, context)
            LOADING -> Utility.showProgress(true, context)
            SUCCESS -> autoFillFields()
            ERROR
            -> {
                Utility.showProgress(false, context)
                Utility.showCustomDialog(
                    context,
                    doRetrieveProfileModel().errorMessage,
                    "",
                    null
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.link_login.setOnClickListener {
            loginScreen()
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

        if (Pref.getValue(context, PrefConstants.IS_LOGIN, false)) {
            rl_my_acc_info.visibility = View.VISIBLE
            link_login.isClickable = false
        } else {
            link_login.visibility = View.VISIBLE
            link_login.text = "Log In"
            link_login.isClickable = true
            rl_my_acc_info.visibility = View.GONE
            link_log_out.visibility = View.GONE
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

        link_account_info.setOnClickListener {
            initScreen()
        }

        link_log_out.setOnClickListener { logoutClearData() }
    }

    private fun autoFillFields() {
        tv_country_name.text = model.profileDomain.responseData?.sqUserdetails?.countryName
        tv_lang_name.text = model.profileDomain.responseData?.sqUserdetails?.languageName
        tv_currency_name.text = model.profileDomain.responseData?.sqUserdetails?.currencyName
        Pref.setValue(context, PrefConstants.EMAIL_ID, model.profileDomain.responseData?.email.toString())
        link_login.text = Pref.getValue(context, PrefConstants.EMAIL_ID, "")
        if (link_login.text == "null")
            link_login.text = " "
        showState(IDLE)
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


    private var loginFrag: LoginFragment? = null

    private fun loginScreen() {
        loginFrag = LoginFragment()
        fragmentManager?.beginTransaction()?.replace(R.id.container_account_info, loginFrag!!)
            ?.addToBackStack("LoginFragment")?.commit()
    }

    private fun init() {
        this.model = ProfileViewModel(context)
        this.presenter = ProfilePresenterImpl(this, context!!)
        if (Pref.getValue(context, PrefConstants.IS_LOGIN, false)) {
            if (null == model.profileDomain.responseData)
                presenter.callAPIGetProfile(Pref.getValue(context, PrefConstants.USER_ID, "0").toString())
        }
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
            "",
            getString(R.string.log_out_messege),
            context?.resources!!.getString(
                R.string.no
            ),
            getString(R.string.yes),
            object : CustomDialogPresenter.CustomDialogView {
                override fun onPositiveButtonClicked() {

                }

                override fun onNegativeButtonClicked() {
                    Pref.setValue(context, PrefConstants.IS_FIRST_TIME, false)
                    Pref.setValue(context, PrefConstants.IS_LOGIN, false)
                    Pref.setValue(context, PrefConstants.USER_ID, "0")
                    Pref.setValue(context, PrefConstants.EMAIL_ID, "")
                    val ft = fragmentManager!!.beginTransaction()
                    ft.detach(this@MyAccountFragment).attach(this@MyAccountFragment).commit()
                }
            })
    }

    override fun onBackPressed(): Boolean {
        if (fragmentManager?.getBackStackEntryAt(0)?.name.equals("SignUpFragment")!!)
            return super.onBackPressed()
        if (fragmentManager?.backStackEntryCount == 0)
            return fragmentManager?.popBackStackImmediate()!!
        else
            return super.onBackPressed()
    }

}
