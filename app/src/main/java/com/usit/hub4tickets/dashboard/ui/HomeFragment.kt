package com.usit.hub4tickets.dashboard.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.HorizontalAdapter
import com.usit.hub4tickets.dashboard.ui.settings.SettingsFragment
import com.usit.hub4tickets.flight.MainFlightActivity
import kotlinx.android.synthetic.main.fragment_home.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    internal var titles = arrayOf(
        "Android",
        "Beta",
        "Cupcake",
        "Donut",
        "Eclair",
        "Froyo",
        "Gingerbread",
        "Honeycomb",
        "Ice Cream Sandwich",
        "Jelly Bean",
        "KitKat",
        "Lollipop",
        "Marshmallow",
        "Nougat",
        "Oreo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firstAdapter = HorizontalAdapter(titles)
        val firstRecyclerView = view.findViewById(R.id.recycler_view) as MultiSnapRecyclerView
        val firstManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        firstRecyclerView.layoutManager = firstManager
        firstRecyclerView.adapter = firstAdapter

        imv_settings.setOnClickListener {

            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout_settings, SettingsFragment.newInstance()!!)
            transaction?.commit()
        }

        ll_flight.setOnClickListener {
            val intent = Intent(context, MainFlightActivity::class.java)
            startActivity(intent)
        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
