package com.usit.hub4tickets.flight.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter
import com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter.MainView.ViewState.*
import com.usit.hub4tickets.domain.presentation.screens.main.FlightPresenterImpl
import com.usit.hub4tickets.flight.adapter.RecyclerViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.search.CommonSearchActivity
import com.usit.hub4tickets.search.model.CommonSelectorPojo
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.SignleSelectionAdapter
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.search_layout.*
import kotlinx.android.synthetic.main.sort_by_dialog.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

class FragmentReturn : RootFragment(), RecyclerViewAdapter.OnItemClickListener, FlightPresenter.MainView {

    val c = Calendar.getInstance()
    private var isItemClicked: Boolean = false
    private var isItemClickedTo: Boolean = false
    private var fromCode: String? = null
    private var toCode: String? = null
    var sortByCode: String? = null
    var travelClassCode: String? = null

    private val FROM_SELECTION_REQUEST = 501
    private val TO_SELECTION_REQUEST = 502

    private var recyclerView: RecyclerView? = null
    private lateinit var model: FlightViewModel
    private lateinit var presenter: FlightPresenter
    private var dataListSortBy: ArrayList<CommonSelectorPojo>? = ArrayList()
    private var dataListTravelClass: ArrayList<CommonSelectorPojo>? = ArrayList()
    private val dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()

    var adapter: RecyclerViewAdapter? = RecyclerViewAdapter(
        items = emptyList(),
        listener = null
    )

    override fun doRetrieveModel(): FlightViewModel = this.model
    override fun showState(viewState: FlightPresenter.MainView.ViewState) {
        when (viewState) {
            IDLE -> Utility.showProgress(false, context)
            LOADING -> Utility.showProgress(true, context)
            SUCCESS_FROM -> {
                Utility.showProgress(false, context)
                openSearchActivityFlightReturn(
                    model.flightViewModel.responseData as ArrayList<FlightViewModel.AirPortDataResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    FROM_SELECTION_REQUEST
                )
            }
            SUCCESS_TO -> {
                Utility.showProgress(false, context)
                openSearchActivityFlightReturn(
                    model.flightViewModel.responseData as ArrayList<FlightViewModel.AirPortDataResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    TO_SELECTION_REQUEST
                )
            }
            FLIGHT_DETAILS_SUCCESS -> {
                setDataToRecyclerViewAdapter(model.flightListViewModel.responseData)
            }
            ERROR
            -> {
                presenter.presentState(IDLE)
                Utility.showCustomDialog(context, doRetrieveModel().errorMessage.message, R.string.alert_failure, null)
            }
        }
    }

    private fun setDataToRecyclerViewAdapter(responseData: List<FlightViewModel.FlightListResponse.ResponseData>?) {
        dataListAll?.clear()
        if (responseData != null) {
            dataListAll?.addAll(responseData)
        }
        adapter = RecyclerViewAdapter(dataListAll!!, this)
        recyclerView!!.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment, container, false
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        /* val items = resources.getStringArray(R.array.tab_A)
         val adapter = RecyclerViewAdapter(items, this)*/
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager as RecyclerView.LayoutManager?
        btn_sort.setOnClickListener {
            sortBy()
            if (null != sortByCode) {
                when (sortByCode) {
                    "price" -> {
                        val sortedList = dataListAll?.sortedWith(
                            compareBy(
                                FlightViewModel.FlightListResponse.ResponseData::price,
                                FlightViewModel.FlightListResponse.ResponseData::price
                            )
                        )
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
        tv_departure.setOnClickListener { Utility.dateDialog(c, activity, tv_departure) }
        tv_return.setOnClickListener { Utility.dateDialog(c, activity, tv_return) }
        btn_class.setOnClickListener {
            selectTravelClass()
        }
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

    private fun init() {
        this.model = FlightViewModel(context)
        this.presenter = FlightPresenterImpl(this, context)

        //sort by
        dataListSortBy?.add(
            CommonSelectorPojo(
                "1",
                getString(R.string.price_text),
                getString(R.string.price_code),
                true
            )
        )
        dataListSortBy?.add(
            CommonSelectorPojo(
                "2",
                getString(R.string.duration_text),
                getString(R.string.duration_code),
                false
            )
        )
        dataListSortBy?.add(
            CommonSelectorPojo(
                "3",
                getString(R.string.morning_arrival_text),
                getString(R.string.morning_arr_code),
                false
            )
        )
        dataListSortBy?.add(
            CommonSelectorPojo(
                "4",
                getString(R.string.morning_departure_text),
                getString(R.string.morning_departure_code),
                false
            )
        )
        dataListSortBy?.add(
            CommonSelectorPojo(
                "5",
                getString(R.string.evening_departure_text),
                getString(R.string.evening_departure_code),
                false
            )
        )
        dataListSortBy?.add(
            CommonSelectorPojo(
                "6",
                getString(R.string.evening_arrival_text),
                getString(R.string.evening_arrival_code),
                false
            )
        )
        //travel class //ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
        dataListTravelClass?.add(
            CommonSelectorPojo(
                "1",
                getString(R.string.economy_text),
                getString(R.string.economy_code),
                true
            )
        )
        dataListTravelClass?.add(
            CommonSelectorPojo(
                "2",
                getString(R.string.pre_economy_text),
                getString(R.string.pre_economy_code),
                false
            )
        )
        dataListTravelClass?.add(
            CommonSelectorPojo(
                "3",
                getString(R.string.business_text),
                getString(R.string.business_code),
                false
            )
        )
        dataListTravelClass?.add(
            CommonSelectorPojo(
                "4",
                getString(R.string.first_text),
                getString(R.string.first_code),
                false
            )
        )
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

    private fun callAPIAirportData(flag: String?, toString: String) {
        presenter.callAPIAirportData(flag, toString)
    }


    override fun onFlightRowClick(responseData: FlightViewModel.FlightListResponse.ResponseData) {
        enterNextFragment(responseData)
    }

    private fun selectTravelClass() {
        val dialogBuilder = AlertDialog.Builder(this.context!!).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)
        dialogView.tv_dialog_header.text = getString(R.string.cabin_class)
        val layoutManager = LinearLayoutManager(context)
        dialogView.selection_list?.layoutManager = layoutManager
        val adapter = SignleSelectionAdapter(
            this.context!!,
            dataListTravelClass!!,
            object : SignleSelectionAdapter.OnClickListener {
                override fun onListItemClick(
                    dataList: ArrayList<CommonSelectorPojo>,
                    position: Int
                ) {
                    travelClassCode = dataList[position].code
                    Log.d("sortby selected", travelClassCode)
                    Log.d("sortby is selected", dataList[position].isSelected.toString())
                    dialogBuilder.dismiss()
                }
            })
        dialogView.selection_list.adapter = adapter
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }


    private fun sortBy() {
        val dialogBuilder = AlertDialog.Builder(this.context!!).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)
        dialogView.tv_dialog_header.text = getString(R.string.sort_by)
        val layoutManager = LinearLayoutManager(context)
        dialogView.selection_list?.layoutManager = layoutManager
        val adapter = SignleSelectionAdapter(
            this.context!!,
            dataListSortBy!!,
            object : SignleSelectionAdapter.OnClickListener {
                override fun onListItemClick(
                    dataList: ArrayList<CommonSelectorPojo>,
                    position: Int
                ) {
                    sortByCode = dataList[position].code
                    Log.d("sortby selected", sortByCode)
                    Log.d("sortby is selected", dataList[position].isSelected.toString())
                    dialogBuilder.dismiss()
                }
            })
        dialogView.selection_list.adapter = adapter
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(false)
        dialogBuilder.show()
    }


    private fun enterNextFragment(responseData: FlightViewModel.FlightListResponse.ResponseData) {
        val intent = Intent(activity?.baseContext, TripDetailsActivity::class.java)
        intent.putExtra(Constant.Path.FLIGHT_DETAILS, responseData)
        startActivity(intent)
    }

    private fun openSearchActivityFlightReturn(
        arrayListAirPortData: ArrayList<FlightViewModel.AirPortDataResponse.ResponseData>,
        title: String,
        selectionRequest: Int
    ) {
        if (arrayListAirPortData.size > 0) {
            val intent = Intent(context, CommonSearchActivity::class.java)
            intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
            intent.putParcelableArrayListExtra(Constant.Path.AIRPORT_FROM_LIST, arrayListAirPortData)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivityForResult(intent, selectionRequest)
        }
    }

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