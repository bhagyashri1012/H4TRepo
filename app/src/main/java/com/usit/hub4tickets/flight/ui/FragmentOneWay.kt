package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter
import com.usit.hub4tickets.domain.presentation.screens.main.FlightPresenterImpl
import com.usit.hub4tickets.flight.adapter.RecyclerViewAdapter
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.search.CommonSearchActivity
import com.usit.hub4tickets.search.model.CommonSelectorPojo
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.SignleSelectionAdapter
import com.usit.hub4tickets.utils.Utility
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.search_layout.*
import kotlinx.android.synthetic.main.sort_by_dialog.view.*
import java.util.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class FragmentOneWay : RootFragment(), RecyclerViewAdapter.OnItemClickListener, FlightPresenter.MainView {

    val c = Calendar.getInstance()
    private var isItemClicked: Boolean = false
    private var isItemClickedTo: Boolean = false
    private var fromCode: String? = null
    private var toCode: String? = null
    private var sortByCode: String? = null
    private var travelClassCode: String? = "ECONOMY"
    private var travelClass: String? = "Economy"
    private val FROM_SELECTION_REQUEST = 601
    private val TO_SELECTION_REQUEST = 602
    private var recyclerView: RecyclerView? = null
    private lateinit var model: FlightViewModel
    private lateinit var presenter: FlightPresenter
    private val dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()

    var adapter: RecyclerViewAdapter? = RecyclerViewAdapter(
        items = emptyList(),
        listener = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_one_way, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDataAndListeners(view)
    }

    override fun doRetrieveModel(): FlightViewModel = this.model
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
            FlightPresenter.MainView.ViewState.FLIGHT_DETAILS_SUCCESS -> {
                setDataToRecyclerViewAdapter(model.flightListViewModel.responseData)
            }
            FlightPresenter.MainView.ViewState.ERROR
            -> {
                dataListAll?.clear()
                adapter?.notifyDataSetChanged()
                presenter.presentState(FlightPresenter.MainView.ViewState.IDLE)
                Utility.showCustomDialog(context, doRetrieveModel().errorMessage.message, "", null)
            }
        }
    }

    override fun onFlightRowClick(responseData: FlightViewModel.FlightListResponse.ResponseData) {
        enterNextFragment(responseData)
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

    override fun onResume() {
        super.onResume()
        edt_from.addTextChangedListener(object : TextWatcherExtended() {
            override fun afterTextChanged(s: Editable, backSpace: Boolean) {
                if (!backSpace) {
                    if (s.length > 2)
                        if (!isItemClicked && isVisible) {
                            callAPIAirportData(Constant.Path.FROM, s.toString())
                        }
                } else {
                    if (s.isEmpty()) {
                        isItemClicked = false
                    }
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        edt_to.addTextChangedListener(
            object : TextWatcherExtended() {
                override fun afterTextChanged(s: Editable, backSpace: Boolean) {
                    if (!backSpace) {
                        if (s.length > 2) {
                            if (!isItemClickedTo && isVisible) {
                                callAPIAirportData(Constant.Path.TO, s.toString())
                            }
                        }
                    } else {
                        if (s.isEmpty())
                            isItemClickedTo = false
                    }
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            })
    }

    override fun onPause() {
        super.onPause()
        edt_from.error = null//removes error
        edt_to.error = null
    }

    private var dataListSortBy: ArrayList<CommonSelectorPojo>? = null
    private var dataListTravelClass: ArrayList<CommonSelectorPojo>? = null
    private fun init() {
        this.model = FlightViewModel(context)
        this.presenter = FlightPresenterImpl(this, context)
        dataListSortBy = ArrayList()
        dataListTravelClass = ArrayList()
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

    private fun setDataAndListeners(v: View) {
        recyclerView = v.findViewById(R.id.recycler_view_one_way) as RecyclerView
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
        tv_departure.text = Utility.getCurrentDateNow()
        tv_departure.setOnClickListener { Utility.dateDialogWithMinMaxDate(c, activity, tv_departure, 0) }
        tv_return.visibility = View.GONE
        btn_class.setOnClickListener { selectTravelClass() }
        btn_class.text = travelClass
        btn_passengers.text = adults + " Adult " +
                childrens + " Children " +
                infants + " Infants "
        btn_passengers.setOnClickListener { selectTravelClass() }
        im_btn_search.setOnClickListener {
            attemptSearch()
        }

    }


    private fun setDataToRecyclerViewAdapter(responseData: List<FlightViewModel.FlightListResponse.ResponseData>?) {
        if (responseData != null) {
            dataListAll?.addAll(responseData)
        }
        adapter = RecyclerViewAdapter(dataListAll!!, this)
        recyclerView!!.adapter = adapter
    }

    private fun enterNextFragment(responseData: FlightViewModel.FlightListResponse.ResponseData) {
        val intent = Intent(activity?.baseContext, TripDetailsActivity::class.java)
        intent.putExtra(Constant.Path.FLIGHT_DETAILS, responseData)
        startActivity(intent)
    }

    private var adults: String? = "1"
    private var childrens: String? = "0"
    private var infants: String? = "0"
    private fun selectTravelClass() {
        val dialogBuilder = AlertDialog.Builder(this.context!!).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)
        dialogView.ll_passenger_info.visibility = View.VISIBLE
        dialogView.ll_apply.visibility = View.VISIBLE
        dialogView.tv_dialog_header.text = getString(R.string.passenger_information)
        dialogView.tv_dialog_header_rcv.text = getString(R.string.cabin_class)
        dialogView.tv_quantity_adult.text = adults
        dialogView.tv_quantity_children.text = childrens
        dialogView.tv_quantity_infants.text = infants
        dialogView.imv_minus_adult.setOnClickListener {
            Utility.onMinusClick(dialogView.tv_quantity_adult, true, false)
        }
        dialogView.imv_plus_adult.setOnClickListener {
            Utility.onAddClick(dialogView.tv_quantity_adult, true, false)
        }
        dialogView.imv_minus_children.setOnClickListener {
            Utility.onMinusClick(dialogView.tv_quantity_children, false, false)
        }
        dialogView.imv_plus_children.setOnClickListener {
            Utility.onAddClick(dialogView.tv_quantity_children, false, false)
        }
        dialogView.imv_minus_infants.setOnClickListener {
            Utility.onMinusClick(dialogView.tv_quantity_infants, false, true)
        }
        dialogView.imv_plus_infants.setOnClickListener {
            Utility.onAddClick(dialogView.tv_quantity_infants, false, true)
        }
        dialogView.button_dialog_apply.setOnClickListener {
            adults = dialogView.tv_quantity_adult.text.toString()
            childrens = dialogView.tv_quantity_children.text.toString()
            infants = dialogView.tv_quantity_infants.text.toString()
            btn_passengers.text = dialogView.tv_quantity_adult.text.toString() + " Adult " +
                    dialogView.tv_quantity_children.text.toString() + " Children " +
                    dialogView.tv_quantity_infants.text.toString() + " Infants "
            btn_class.text = travelClass
            dialogBuilder.dismiss()
        }
        dialogView.button_dialog_cancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
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
                    travelClass = dataList[position].itemsName
                }
            })

        dialogView.selection_list.adapter = adapter
        dialogBuilder.setView(dialogView)
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
        dialogBuilder.show()
    }

    private fun openSearchActivityFlightReturn(
        arrayListAirPortData: ArrayList<FlightViewModel.AirPortDataResponse.ResponseData>,
        title: String,
        selectionRequest: Int
    ) {
        if (arrayListAirPortData.size > 0) {
            val intent = Intent(context, CommonSearchActivity::class.java)
            intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
            intent.putParcelableArrayListExtra(Constant.Path.AIRPORT_RETURN_LIST, arrayListAirPortData)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivityForResult(intent, selectionRequest)
        }
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


    private fun callAPIAirportData(flag: String?, toString: String) {
        presenter.callAPIAirportData(flag, toString)
    }

    private fun attemptSearch() {
        // Reset errors.
        edt_from.error = null
        edt_to.error = null
        val edtFrom = edt_from.text.toString()
        val edtTo = edt_to.text.toString()
        val departureDate = tv_departure.text.toString()
        val returnDate = tv_return.text.toString()
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(edtFrom)) {
            edt_from.error = getString(R.string.error_field_required_from_airport)
            focusView = edt_from
            cancel = true
        } else if (TextUtils.isEmpty(edtTo)) {
            edt_to.error = getString(R.string.error_field_required_to_airport)
            focusView = edt_to
            cancel = true
        } else if (TextUtils.isEmpty(departureDate)) {
            CustomDialogPresenter.showDialog(
                context,
                "",
                getString(R.string.error_field_required_departure),
                context!!.resources.getString(
                    R.string.ok
                ),
                null,
                null
            )
            focusView = tv_departure
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            Utility.showProgress(true, context)
            presenter.callFlightDetails(
                adults!!,
                travelClassCode!!,//ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
                childrens!!,
                tv_departure.text.toString(),
                getString(R.string.oneway),
                fromCode.toString(),
                toCode.toString(),
                infants!!,
                "in-EN",
                ""
            )
        }
    }

}