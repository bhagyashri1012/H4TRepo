package com.usit.hub4tickets.flight.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by Bhagyashri Burade
 * Date: 14/01/2019
 * Email: bhagyashri.burade@usit.net.in
 */

class FilterFragment : RootDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleToolBar.text = resources.getString(R.string.my_account_info)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FilterFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.popBackStackImmediate()!!
    }
}
