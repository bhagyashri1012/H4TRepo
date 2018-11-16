package com.usit.hub4tickets.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.ui.RootFragment

class AccountInfoFragment : RootFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_info, container, false)
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Account Information"
    }*/

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
