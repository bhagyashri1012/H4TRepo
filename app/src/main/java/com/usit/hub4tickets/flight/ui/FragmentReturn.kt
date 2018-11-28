package com.usit.hub4tickets.flight.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter
import com.usit.hub4tickets.domain.presentation.screens.main.FlightPresenterImpl
import com.usit.hub4tickets.flight.adapter.RecyclerViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.search.CommonSearchActivity
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.search_layout.*
import kotlinx.android.synthetic.main.sort_by_dialog.view.*
import java.util.*


class FragmentReturn : RootFragment(), RecyclerViewAdapter.OnItemClickListener, FlightPresenter.MainView {

    val c = Calendar.getInstance()
    private val FROM_SELECTION_REQUEST = 501
    private val TO_SELECTION_REQUEST = 502

    private var recyclerView: RecyclerView? = null
    private lateinit var model: FlightViewModel
    private lateinit var presenter: FlightPresenter

    override fun doRetrieveModel(): FlightViewModel = this.model
    @SuppressLint("NewApi")
    override fun showState(viewState: FlightPresenter.MainView.ViewState) {
        when (viewState) {
            FlightPresenter.MainView.ViewState.IDLE -> Utility.showProgress(false, context)
            FlightPresenter.MainView.ViewState.LOADING -> Utility.showProgress(true, context)
            FlightPresenter.MainView.ViewState.SUCCESS_FROM -> {
                Utility.showProgress(false, context)
                openSearchActivityFlightReturn(
                    model.flightViewModel.responseData as ArrayList<FlightViewModel.AirPortDataResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    FROM_SELECTION_REQUEST
                )
            }
            FlightPresenter.MainView.ViewState.SUCCESS_TO -> {
                Utility.showProgress(false, context)
                openSearchActivityFlightReturn(
                    model.flightViewModel.responseData as ArrayList<FlightViewModel.AirPortDataResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    TO_SELECTION_REQUEST
                )
            }
            FlightPresenter.MainView.ViewState.FLIGHT_DETAILS_SUCCESS -> Utility.showProgress(false, context)
            FlightPresenter.MainView.ViewState.ERROR
            -> {
                presenter.presentState(FlightPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(context, doRetrieveModel().errorMessage.message, R.string.alert_failure, null)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment, container, false
        )

        return view
    }


    abstract inner class TextWatcherExtended : TextWatcher {

        private var lastLength: Int = 0

        abstract fun afterTextChanged(s: Editable, backSpace: Boolean)

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            lastLength = s.length
        }

        override fun afterTextChanged(s: Editable) {
            afterTextChanged(s, lastLength > s.length)
        }
    }

    private var isItemClicked: Boolean = false
    private var isItemClickedTo: Boolean = false

    override fun onResume() {
        super.onResume()
        edt_from.addTextChangedListener(object : TextWatcherExtended() {
            override fun afterTextChanged(s: Editable, backSpace: Boolean) {
                // Here you are! You got missing "backSpace" flag
                if (!backSpace) {
                    if (s?.length!! > 2)
                        if (!isItemClicked && isVisible) {

                            callAPIAirportData(Constant.Path.FROM, s.toString())
                        }
                } else {
                    if (s?.length!! == 0)
                        isItemClicked = false
                }
            }


            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do something useful if you wish.
                // Or override it in TextWatcherExtended class if want to avoid it here

            }
        })

        edt_to.addTextChangedListener(
            object : TextWatcherExtended() {

                override fun afterTextChanged(s: Editable, backSpace: Boolean) {
                    // Here you are! You got missing "backSpace" flag
                    if (!backSpace) {
                        if (s?.length!! > 2) {
                            if (!isItemClickedTo && isVisible) {

                                callAPIAirportData(Constant.Path.TO, s.toString())
                            }
                        }
                    } else {
                        if (s?.length!! == 0)
                            isItemClickedTo = false
                    }
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // Do something useful if you wish.
                    // Or override it in TextWatcherExtended class if want to avoid it here
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        val items = resources.getStringArray(R.array.tab_A)
        val adapter = RecyclerViewAdapter(items, this)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager as RecyclerView.LayoutManager?
        recyclerView!!.adapter = adapter
        btn_sort.setOnClickListener { sortBy() }
        tv_departure.setOnClickListener { Utility.dateDialog(c, activity, tv_departure) }
        tv_return.setOnClickListener { Utility.dateDialog(c, activity, tv_return) }

        im_btn_search.setOnClickListener {
            presenter.callFlightDetails(
                "1",
                "ECONOMY",//ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
                "1",
                tv_departure.text.toString(),
                "round",
                fromCode.toString(),
                toCode.toString(),
                "0",
                "in-EN",
                tv_return.text.toString()
            )
        }
    }

    private fun callAPIAirportData(flag: String?, toString: String) {
        presenter.callAPIAirportData(flag, toString)
    }

    private fun init() {
        this.model = FlightViewModel(context)
        this.presenter = FlightPresenterImpl(this, context)
    }

    override fun onFlightRowClick() {
        enterNextFragment()
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

    private fun openSearchActivityFlightReturn(
        arrayListAirPortData: ArrayList<FlightViewModel.AirPortDataResponse.ResponseData>,
        title: String,
        selectionRequest: Int
    ) {
        val intent = Intent(context, CommonSearchActivity::class.java)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        intent.putParcelableArrayListExtra(Constant.Path.AIRPORT_FROM_LIST, arrayListAirPortData)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivityForResult(intent, selectionRequest)

    }

    private var fromCode: String? = null
    private var toCode: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FROM_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    isItemClicked = true
                    edt_from.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME))
                    fromCode = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_TYPE)

                }
            }
            TO_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    isItemClickedTo = true
                    edt_to.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME))
                    toCode = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_TYPE)
                }
            }
        }
    }
}