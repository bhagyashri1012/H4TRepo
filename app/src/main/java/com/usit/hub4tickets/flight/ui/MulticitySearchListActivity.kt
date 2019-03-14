package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.usit.hub4tickets.utils.pagination.PaginationAdapterCallback
import com.usit.hub4tickets.utils.pagination.PaginationScrollListener
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.activity_multicity_search_list.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.sort_by_dialog.view.*

class MulticitySearchListActivity : BaseActivity(), FlightPresenter.MainView,
    MultiCityInnerAdapter.OnItemClickListener, PaginationAdapterCallback {

    private var travelClass: String? = "Economy"
    private var travelClassCode: String? = "0"
    private var multicityParamList: java.util.ArrayList<FlightViewModel.MultiCitiesForSearch1>? = java.util.ArrayList()
    private var data: FlightViewModel.MultiCityResponse? = null
    private var filterData: FilterModel.Filter? = null
    private val Filter_SELECTION_REQUEST = 503
    private lateinit var model: FlightViewModel
    private lateinit var presenter: FlightPresenter
    private var totalPassengers: String = ""
    private var openFilter: Boolean = false
    private var currency: String? = ""
    private var dataListSortBy: ArrayList<CommonSelectorPojo>? = null
    private var sortByCode: String? = null

    //Pagination variables
    val PAGE_START = 0
    var isLoadingOn = false
    var isLastPg = false
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    var TOTAL_PAGES = 20
    var currentPage = PAGE_START

    private var isFilterEnable: Boolean = false

    override fun retryPageLoad() {
        loadNextPage()
    }

    private val TAG = "MulticitySearchList"

    private fun loadNextPage() {
        //load netx page here
        Log.d(TAG, "loadNextPage: $currentPage")
        callMulticityDetailsApi(isFilterEnable)

    }

    private var dataListAll: ArrayList<ResponseDataMulticity>? = ArrayList()
    var adapter: MultiCityRecyclerViewAdapter? =
        MultiCityRecyclerViewAdapter(
            items = null,
            listener = null,
            totalPassengers = "",
            price = "",
            currency = "",
            mCallback = this
        )


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
                rl_flight_not_found.visibility = View.GONE
                isLastPg = model.multiCityListViewModel.last
                if (openFilter) {
                    dataListAll?.clear()
                    adapter?.clear()
                    setDataToRecyclerViewAdapter(model.multiCityListViewModel.responseData as ArrayList<ResponseDataMulticity>)

                } else {
                    setDataToRecyclerViewAdapter(model.multiCityListViewModel.responseData as ArrayList<ResponseDataMulticity>)
                }
            }
            FlightPresenter.MainView.ViewState.MULTICITY_DETAILS_SUCCESS_NEXT_PAGE -> {
                rl_flight_not_found.visibility = View.GONE
                isLastPg = model.multiCityListViewModel.last
                setDataToRecyclerViewAdapterNextPage(model.multiCityListViewModel.responseData as ArrayList<ResponseDataMulticity>?)
            }
            FlightPresenter.MainView.ViewState.STOP_LOADING
            -> adapter!!.removeLoadingFooter()
            FlightPresenter.MainView.ViewState.FLIGHT_NOT_FOUND
            -> {
                rl_flight_not_found.visibility = View.VISIBLE
                dataListAll?.clear()
                adapter?.clear()
                if (!openFilter) {

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
            }
            FlightPresenter.MainView.ViewState.ERROR
            -> {
                if (dataListAll?.size != 0) {
                    rl_flight_not_found.visibility = View.GONE
                }
                when (currentPage) {
                    0 -> Utility.showCustomDialog(
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
                    else -> adapter?.showRetry(true, doRetrieveModel().errorMessage.message)

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multicity_search_list)
        init()
        setDataAndListeners()
    }

    private fun setDataAndListeners() {
        val layoutManager = LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager as RecyclerView.LayoutManager?
        //scroller for pagination
        recycler_view!!.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                isLoadingOn = true
                currentPage += 1
                if (!isLastPg)
                    loadNextPage()
                else
                    isLoadingOn = false
            }

            override fun isLastPage(): Boolean {
                return isLastPg
            }

            override fun isLoading(): Boolean {
                return isLoadingOn

            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

        })
        if (null != intent.extras) {
            travelClassCode = intent.getStringExtra(Constant.Path.CABIN_CLASS_CODE)
            multicityParamList =
                intent.getParcelableArrayListExtra<FlightViewModel.MultiCitiesForSearch1>((Constant.Path.MULTICITY_SEARCH_PARAMS))
            totalPassengers = intent.getStringExtra(Constant.Path.TOTAL_PASSENGERS)
            isFilterEnable=false
            callMulticityDetailsApi(isFilterEnable)
        }
    }

    private fun init() {
        this.model = FlightViewModel(this)
        this.presenter = FlightPresenterImpl(this, this)
        title = resources.getString(R.string.multicity)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }

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

    private fun openFilter() {
        openFilter = true
        val intent = Intent(this, FilterActivity::class.java)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, "FragmentMulticity")
        intent.putExtra(Constant.Path.FILTER_DATA, filterData)
        intent.putExtra(Constant.Path.MAX_PRICE, model.multiCityListViewModel.maxPrice.toString())
        intent.putExtra(Constant.Path.MIN_PRICE, model.multiCityListViewModel.minPrice.toString())
        startActivityForResult(intent, Filter_SELECTION_REQUEST)
    }


    private fun callMulticityDetailsApi(isFiltering: Boolean) {
        if (Pref.getValue(this, PrefConstants.CURRENCY, "")!!.equals(""))
            currency = Pref.getValue(this, PrefConstants.CURRENCY_DEFAULT, "GB")
        else
            currency = Pref.getValue(this, PrefConstants.CURRENCY, "")

        presenter.callMulticityDetails(
            currency!!,
            Pref.getValue(this, PrefConstants.USER_ID, "0").toString(),
            "in-EN",
            multicityParamList,
            filterData?.price_from,
            filterData?.price_to,
            filterData?.max_fly_duration,
            travelClassCode.toString(),//ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST\
            Pref.getValue(this, PrefConstants.USER_ID, "0").toString(),
            currentPage,
            TOTAL_PAGES,
            isFiltering
        )
    }

    /* private fun setDataToRecyclerViewAdapter(
         responseData: ArrayList<ResponseDataMulticity>?
     ) {
         adapter = MultiCityRecyclerViewAdapter(responseData, this, totalPassengers, "", currency!!, this)
         recycler_view!!.adapter = adapter
     }*/
    private fun setDataToRecyclerViewAdapter(
        responseData: List<ResponseDataMulticity>?
    ) {
        dataListAll?.clear()
        if (responseData != null) {
            adapter = MultiCityRecyclerViewAdapter(ArrayList(), this, totalPassengers, "", currency!!, this)
            recycler_view!!.adapter = adapter

            dataListAll!!.addAll(responseData)
            loadFirstPage(dataListAll)
        }

    }

    private fun loadFirstPage(dataListAll: ArrayList<ResponseDataMulticity>?) {
        isLoadingOn = false
        adapter!!.addAll(dataListAll!!)

        if (!isLastPg)
            adapter!!.addLoadingFooter()
        else
            adapter!!.removeLoadingFooter()

    }

    /**
     * This method is to set data to RecyclerViewAdapter on scroll
     * pagination implementation : -
     * remove loading footer -> set loading flag =0 -> add response data to arraylist
     * add loading footer if isLastPag=false
     *
     * @param List<FlightViewModel.FlightListResponse.ResponseData>?
     */
    private fun setDataToRecyclerViewAdapterNextPage(
        responseData: ArrayList<ResponseDataMulticity>?
    ) {
        adapter!!.removeLoadingFooter()
        isLoadingOn = false
        adapter!!.addAll(responseData!!)

        if (!isLastPg)
            adapter!!.addLoadingFooter()

    }

    override fun onMulticityRowClick(
        responseData: List<FlightViewModel.MultiCityResult>,
        totalPassengers: String,
        price: String,
        deepLink: String,
        currency: String
    ) {
        enterNextFragment(
            responseData as ArrayList<FlightViewModel.MultiCityResult>,
            totalPassengers,
            price,
            deepLink,
            currency
        )
    }

    private fun enterNextFragment(
        multiCityResult: ArrayList<FlightViewModel.MultiCityResult>,
        totalPassengers: String,
        price: String,
        deepLink: String,
        currency: String
    ) {
        val intent = Intent(this, MulticityTripDetailsActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.MULTICITY_LIST_DETAILS, multiCityResult)
        intent.putExtra(Constant.Path.CURRENCYSYMBOL, dataListAll!![0].currencySymbol)
        intent.putExtra(Constant.Path.PRICE, price)
        intent.putExtra(Constant.Path.URL, deepLink)
        intent.putExtra(Constant.Path.TOTAL_PASSENGERS, totalPassengers)
        intent.putExtra(Constant.Path.CABIN_CLASS, travelClass)
        startActivity(intent)
    }

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
                    commonSelectorPojo: ArrayList<CommonSelectorPojo>,
                    position: Int
                ) {
                    sortByCode = commonSelectorPojo[position].code
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
                        sortedList!![c].totalDurationFormatted =
                            Utility.getDurationFromString(dataListAll!![c].totalDuration).toString()
                    }
                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }
                    sortedList?.sortBy { it.price.toDouble() }
                    setDataToRecyclerViewAdapter(sortedList)
                }
                resources.getString(R.string.duration_code) -> {
                    val sortedList: ArrayList<ResponseDataMulticity>? =
                        ArrayList()
                    sortedList?.addAll(dataListAll!!)
                    for (c in dataListAll?.indices!!) {
                        sortedList!![c].totalDurationFormatted =
                            Utility.getDurationFromString(dataListAll!![c].totalDuration).toString()
                    }
                    sortedList?.sortBy { it.price.toDouble() }
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
                    currentPage = 0
                    dataListAll!!.clear()
                    adapter!!.clear()
                    isFilterEnable=true
                    callMulticityDetailsApi(isFilterEnable)
                }
            }
        }
    }
}