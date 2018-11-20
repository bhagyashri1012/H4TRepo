package com.usit.hub4tickets.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.ui.RootFragment
import com.usit.hub4tickets.profile.ui.PersonalInfoActivity
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import kotlinx.android.synthetic.main.fragment_account_info.*

class AccountInfoFragment : RootFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cv_personal_info.setOnClickListener {
            if (Pref.getValue(context, PrefConstants.IS_LOGIN, false))
                redirectToPersonalInfo()
        }
    }

    private fun redirectToPersonalInfo() {
        val intent = Intent(context, PersonalInfoActivity::class.java)
        startActivity(intent)
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
