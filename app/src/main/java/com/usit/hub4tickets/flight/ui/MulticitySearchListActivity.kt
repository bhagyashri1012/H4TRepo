package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.FlightPresenterImpl
import com.usit.hub4tickets.flight.adapter.MultiCityInnerAdapter
import com.usit.hub4tickets.flight.adapter.MultiCityRecyclerViewAdapter
import com.usit.hub4tickets.flight.model.FilterModel
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.flight.model.FlightViewModel.ResponseDataMulticity
import com.usit.hub4tickets.search.model.CommonSelectorPojo
import com.usit.hub4tickets.utils.*
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.activity_multicity_search_list.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.sort_by_dialog.view.*

class MulticitySearchListActivity : BaseActivity(), FlightPresenter.MainView,
    MultiCityInnerAdapter.OnItemClickListener {
    private var dataListAll: ArrayList<ResponseDataMulticity>? = ArrayList()
    private var totalPassengers: String = ""
    private var adults: String? = "1"
    private var children: String? = "0"
    private var infants: String? = "0"
    var adapter: MultiCityRecyclerViewAdapter? =
        MultiCityRecyclerViewAdapter(
            items = null,
            listener = null,
            totalPassengers = "",
            price = "",
            currency = ""
        )
    private var travelClass: String? = "Economy"
    private var travelClassCode: String? = "0"
    var multicityParamList: java.util.ArrayList<FlightViewModel.MultiCitiesForSearch1>? = java.util.ArrayList()
    var data: FlightViewModel.MultiCityResponse? = null
    var filterData: FilterModel.Filter? = null
    private val Filter_SELECTION_REQUEST = 503
    private lateinit var model: FlightViewModel
    private lateinit var presenter: FlightPresenter

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    override fun doRetrieveModel(): FlightViewModel = this.model

    override fun showState(viewState: FlightPresenter.MainView.ViewState) {
        when (viewState) {
            FlightPresenter.MainView.ViewState.IDLE -> {
                Utility.showProgress(false, this)
            }
            FlightPresenter.MainView.ViewState.LOADING -> {
                Utility.showProgress(true, this)
            }
            FlightPresenter.MainView.ViewState.MULTICITY_DETAILS_SUCCESS -> {
                rl_flight_not_found.visibility= View.GONE

                if (openFilter) {
                    dataListAll?.clear()
                    dataListAll?.addAll(model.multiCityListViewModel.responseData!!)
                    adapter?.notifyDataSetChanged()
                } else {

                    dataListAll = model.multiCityListViewModel.responseData as ArrayList<ResponseDataMulticity>
                    setDataToRecyclerViewAdapter(dataListAll)
                }
            }
            FlightPresenter.MainView.ViewState.FLIGHT_NOT_FOUND
            -> {
                rl_flight_not_found.visibility= View.VISIBLE
                Utility.showCustomDialog(
                    this,
                    doRetrieveModel().errorMessage.message,
                    "",
                    object : CustomDialogPresenter.CustomDialogView {
                        override fun onNegativeButtonClicked() {
                        }

                        override fun onPositiveButtonClicked() {
                            onBackPressed()
                        }

                    })
            }
            FlightPresenter.MainView.ViewState.ERROR
            -> {
                Utility.showCustomDialog(
                    this,
                    doRetrieveModel().errorMessage.message,
                    "",
                    object : CustomDialogPresenter.CustomDialogView {
                        override fun onNegativeButtonClicked() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onPositiveButtonClicked() {
                            onBackPressed()
                        }

                    })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multicity_search_list)
        init()
        if (null != intent.extras) {
            // data = intent.getParcelableExtra(Constant.Path.MULTICITY_DETAILS)
            travelClassCode = intent.getStringExtra(Constant.Path.CABIN_CLASS_CODE)
            multicityParamList =
                intent.getParcelableArrayListExtra<FlightViewModel.MultiCitiesForSearch1>((Constant.Path.MULTICITY_SEARCH_PARAMS))
            //maxPrice = data?.maxPrice.toString()
            //minPrice = data?.minPrice.toString()
            totalPassengers = intent.getStringExtra(Constant.Path.TOTAL_PASSENGERS)
            /*if (null != data) {
                dataListAll = data?.responseData as ArrayList<ResponseDataMulticity>

            }*/
            callMulticityDetailsApi(filterData)

        }
    }

    private fun init() {
        this.model = FlightViewModel(this)
        this.presenter = FlightPresenterImpl(this, this)
        title = resources.getString(R.string.multicity)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        val layoutManager = LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager
        dataListSortBy = ArrayList()
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

        btn_sort.setOnClickListener {
            if (dataListAll!!.isNotEmpty())
                sortByDailog()
        }
        btn_filter.setOnClickListener {
            openFilter()
        }
    }

    var openFilter: Boolean = false
    private fun openFilter() {
        openFilter = true
        val intent = Intent(this, FilterActivity::class.java)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, "FragmentMulticity")
        intent.putExtra(Constant.Path.FILTER_DATA, filterData)
        intent.putExtra(Constant.Path.MAX_PRICE, model.multiCityListViewModel.maxPrice.toString())
        intent.putExtra(Constant.Path.MIN_PRICE, model.multiCityListViewModel.minPrice.toString())
        startActivityForResult(intent, Filter_SELECTION_REQUEST)
    }
    private var currency: String? = ""

    private fun callMulticityDetailsApi(filterData: FilterModel.Filter?) {
        if(Pref.getValue(this, PrefConstants.CURRENCY_ID, "")!!.equals(""))
            currency = Pref.getValue(this, PrefConstants.CURRENCY_DEFAULT, "")
        else
            currency=""

        presenter.callMulticityDetails(
            adults!!,
            children!!,
            currency!!,
            Pref.getValue(this, PrefConstants.USER_ID, "0").toString(),
            infants!!,
            "in-EN",
            multicityParamList,
            filterData?.price_from,
            filterData?.price_to,
            filterData?.max_fly_duration,
            travelClassCode.toString(),//ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST\
            Pref.getValue(this, PrefConstants.USER_ID, "0").toString()
        )
    }

    private fun setDataToRecyclerViewAdapter(
        responseData: ArrayList<ResponseDataMulticity>?
    ) {
        adapter = MultiCityRecyclerViewAdapter(responseData, this, totalPassengers, "", "")
        recycler_view!!.adapter = adapter
    }

    override fun onMulticityRowClick(
        responseData: List<FlightViewModel.MultiCityResult>,
        totalPassengers: String,
        price: String
    ) {
        enterNextFragment(responseData as ArrayList<FlightViewModel.MultiCityResult>, totalPassengers, price)
    }

    private fun enterNextFragment(
        responseData: ArrayList<FlightViewModel.MultiCityResult>,
        totalPassengers: String,
        price: String
    ) {
        val intent = Intent(this, MulticityTripDetailsActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.MULTICITY_LIST_DETAILS, responseData)
        intent.putExtra(Constant.Path.PRICE, price)
        intent.putExtra(Constant.Path.TOTAL_PASSENGERS, totalPassengers)
        intent.putExtra(Constant.Path.CABIN_CLASS, travelClass)
        startActivity(intent)
    }

    private var dataListSortBy: ArrayList<CommonSelectorPojo>? = null
    private var sortByCode: String? = null

    private fun sortByDailog() {
        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)
        dialogView.tv_dialog_header.text = getString(R.string.sort_by)
        val layoutManager = LinearLayoutManager(this)
        dialogView.selection_list?.layoutManager = layoutManager
        val adapter = SignleSelectionSortAdapter(
            this,
            dataListSortBy!!, true,
            object : SignleSelectionSortAdapter.OnClickListener {
                override fun onListItemClick(
                    dataList: ArrayList<CommonSelectorPojo>,
                    position: Int
                ) {
                    sortByCode = dataList[position].code
                    sortByForMulticity(sortByCode)
                    dialogBuilder.dismiss()
                }
            })
        dialogView.selection_list.adapter = adapter
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCanceledOnTouchOutside(true)
        dialogBuilder.show()
    }

    private fun sortByForMulticity(sortByCode: String?) {
        if (null != sortByCode) {
            when (sortByCode) {
                resources.getString(R.string.price_code) -> {
                    val sortedList: ArrayList<ResponseDataMulticity>? =
                        ArrayList()
                    sortedList?.addAll(dataListAll!!)
                    for (c in dataListAll?.indices!!) {
                        sortedList!![c]?.totalDurationFormatted =
                            Utility.getDurationFromString(dataListAll!![c].totalDuration).toString()
                    }
                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }
                    sortedList?.sortBy { it.price?.toDouble() }
                    setDataToRecyclerViewAdapter(sortedList)
                }
                resources.getString(R.string.duration_code) -> {
                    val sortedList: ArrayList<ResponseDataMulticity>? =
                        ArrayList()
                    sortedList?.addAll(dataListAll!!)
                    for (c in dataListAll?.indices!!) {
                        sortedList!![c]?.totalDurationFormatted =
                            Utility.getDurationFromString(dataListAll!![c].totalDuration).toString()
                    }
                    sortedList?.sortBy { it.price?.toDouble() }
                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }
                    for (c in sortedList?.indices!!) {
                        Log.d("duration--", sortedList[c].totalDurationFormatted.toString())
                    }
                    setDataToRecyclerViewAdapter(sortedList)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Filter_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    filterData = data?.getParcelableExtra(Constant.Path.FILTER_DATA)
                    callMulticityDetailsApi(filterData)
                }
            }
        }
    }
}