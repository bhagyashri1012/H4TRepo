package com.usit.hub4tickets.dashboard.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance(): HelpFragment {
            return HelpFragment()
        }
    }
}
