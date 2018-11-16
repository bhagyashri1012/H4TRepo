package com.usit.hub4tickets.dashboard.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.login.ui.LoginActivity
import com.usit.hub4tickets.profile.AccountInfoFragment
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Pref.getValue(context, PrefConstants.IS_LOGIN, false)) {
            rl_my_acc_info.visibility = View.VISIBLE
            link_login.isClickable = false
            link_login.text = Pref.getValue(context, PrefConstants.EMAIL_ID, "")

        } else {
            link_login.visibility = View.VISIBLE
            link_login.isClickable = true
            rl_my_acc_info.visibility = View.GONE
        }
        link_account_info.setOnClickListener {
            initScreen()
        }

        link_log_out.setOnClickListener { logoutClearData() }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    private var accountMainFrag: AccountInfoFragment? = null

    private fun initScreen() {
        accountMainFrag = AccountInfoFragment()
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()?.replace(R.id.container_account_info, accountMainFrag!!)?.addToBackStack("AccountInfo")?.commit()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
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
                    Pref.setValue(context, PrefConstants.USER_ID, false)
                    Pref.setValue(context, PrefConstants.EMAIL_ID, "")
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }

                override fun onNegativeButtonClicked() {

                }
            })

    }
}
