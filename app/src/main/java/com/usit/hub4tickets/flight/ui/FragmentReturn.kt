package com.usit.hub4tickets.flight.ui


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.adapter.RecyclerViewAdapter
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.search_layout.*
import kotlinx.android.synthetic.main.sort_by_dialog.view.*
import java.util.*


class FragmentReturn : RootFragment(), RecyclerViewAdapter.OnItemClickListener {

    val c = Calendar.getInstance()
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0

    override fun onFlightRowClick() {
        enterNextFragment()
    }

    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment, container, false
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = resources.getStringArray(R.array.tab_A)
        val adapter = RecyclerViewAdapter(items, this)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = adapter


        btn_sort.setOnClickListener { sortBy() }
        tv_departure.setOnClickListener { Utility.dateDialog(c,activity,tv_departure) }
        txt_return.setOnClickListener { Utility.dateDialog(c,activity,txt_return) }


    }

    private fun sortBy() {
        val dialogBuilder = AlertDialog.Builder(this.context!!).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)
        dialogView.radioButton1.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogView.radioButton2.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }

    private fun enterNextFragment() {
        val intent = Intent(activity?.baseContext, TripDetailsActivity::class.java)
        intent.putExtra("SCREEN_NAME", "home")
        startActivity(intent)
    }
}