package com.usit.hub4tickets.flight.uiimport android.app.Activityimport android.content.Intentimport android.os.Bundleimport android.support.v7.app.AlertDialogimport android.support.v7.widget.LinearLayoutManagerimport android.support.v7.widget.RecyclerViewimport android.text.TextUtilsimport android.util.Logimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.common.RootFragmentimport com.usit.hub4tickets.domain.presentation.presenters.FlightPresenterimport com.usit.hub4tickets.domain.presentation.screens.main.FlightPresenterImplimport com.usit.hub4tickets.flight.adapter.RecyclerViewAdapterimport com.usit.hub4tickets.flight.model.FilterModelimport com.usit.hub4tickets.flight.model.FlightViewModelimport com.usit.hub4tickets.search.AirportsSearchActivityimport com.usit.hub4tickets.search.model.CommonSelectorPojoimport com.usit.hub4tickets.utils.*import com.usit.hub4tickets.utils.pagination.PaginationAdapterCallbackimport com.usit.hub4tickets.utils.pagination.PaginationScrollListenerimport com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenterimport kotlinx.android.synthetic.main.fragment_one_way.*import kotlinx.android.synthetic.main.search_layout.*import kotlinx.android.synthetic.main.sort_by_dialog.view.*import java.util.*import kotlin.collections.ArrayList/** * Created by Bhagyashri Burade * Date: 24/10/2018 * Email: bhagyashri.burade@usit.net.in */class FragmentOneWay : RootFragment(), RecyclerViewAdapter.OnItemClickListener, FlightPresenter.MainView,    PaginationAdapterCallback {    val c = Calendar.getInstance()    private var isItemClicked: Boolean = false    private var isItemClickedTo: Boolean = false    private var fromCode: String? = null    private var toCode: String? = null    private var sortByCode: String? = null    private var travelClassCode: String? = "ECONOMY"    private var travelClass: String? = "Economy"    private val FROM_SELECTION_REQUEST = 601    private val TO_SELECTION_REQUEST = 602    private val Filter_SELECTION_REQUEST = 603    private val CALENDER_SELECTION_REQUEST = 604    private var recyclerView: RecyclerView? = null    private lateinit var model: FlightViewModel    private lateinit var presenter: FlightPresenter    private val dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()    private var totalPassengers: String = ""    private var dataListSortBy: ArrayList<CommonSelectorPojo>? = null    private var dataListTravelClass: ArrayList<CommonSelectorPojo>? = null    private var selectedFromDt: String? = ""    private var selectedToDt: String? = ""    private var filterData: FilterModel.Filter? = null    private var maxPrice: String? = "0"    private var minPrice: String? = "0"    private var fromCity: String? = null    private var toCity: String? = null    private var isSerachClicked: Boolean = false    private var currency: String? = ""    private var isFiltering = false    //Pagination variables    val PAGE_START = 0    var isLoadingOn = false    var isLastPg = false    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.    var TOTAL_PAGES = 20    var currentPage = PAGE_START    var adapter: RecyclerViewAdapter? = RecyclerViewAdapter(        items = ArrayList(),        listener = null,        totalPassengers = null,        className = javaClass.simpleName.toString(),        mCallback = this    )    override fun doRetrieveModel(): FlightViewModel = this.model    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        retainInstance = true    }    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {        return inflater.inflate(R.layout.fragment_one_way, container, false)    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        init()        setDataAndListeners(view)    }    override fun showState(viewState: FlightPresenter.MainView.ViewState) {        when (viewState) {            FlightPresenter.MainView.ViewState.IDLE -> Utility.showProgress(false, context)            FlightPresenter.MainView.ViewState.LOADING -> Utility.showProgress(true, context)            FlightPresenter.MainView.ViewState.FLIGHT_DETAILS_SUCCESS -> {                flight_not_found.visibility = View.GONE                maxPrice = model.flightListViewModel.maxPrice                minPrice = model.flightListViewModel.minPrice                //TOTAL_PAGES = model.flightListViewModel.totalPages                isLastPg = model.flightListViewModel.last                setDataToRecyclerViewAdapter(model.flightListViewModel.responseData as List<FlightViewModel.FlightListResponse.ResponseData>?)                sortByOneWay(sortByCode)            }            FlightPresenter.MainView.ViewState.FLIGHT_DETAILS_SUCCESS_NEXT_PAGE -> {                flight_not_found.visibility = View.GONE                maxPrice = model.flightListViewModel.maxPrice                minPrice = model.flightListViewModel.minPrice                //TOTAL_PAGES = model.flightListViewModel.totalPages                isLastPg = model.flightListViewModel.last                setDataToRecyclerViewAdapterNextPage(model.flightListViewModel.responseData as List<FlightViewModel.FlightListResponse.ResponseData>?)                sortByOneWay(sortByCode)            }            FlightPresenter.MainView.ViewState.STOP_LOADING            -> adapter!!.removeLoadingFooter()            FlightPresenter.MainView.ViewState.FLIGHT_NOT_FOUND            -> {                dataListAll?.clear()                adapter?.clear()                flight_not_found.visibility = View.VISIBLE            }            FlightPresenter.MainView.ViewState.ERROR            -> {                if (dataListAll?.size != 0) {                    flight_not_found.visibility = View.GONE                }                when (currentPage) {                    0 ->                        Utility.showCustomDialog(context, doRetrieveModel().errorMessage.message, "", null)                    else -> adapter?.showRetry(true, doRetrieveModel().errorMessage.message)                }            }        }    }    override fun retryPageLoad() {        loadNextPage()    }    private val TAG = "FragmentOneWay"    private fun loadNextPage() {        //load netx page here        Log.d(TAG, "loadNextPage: $currentPage")        callFlightDetailsApi(filterData, isFiltering)    }    /**     * This method is to set data to RecyclerViewAdapter on scroll     * pagination implementation : -     * remove loading footer -> set loading flag =0 -> add response data to arraylist     * add loading footer if isLastPag=false     *     * @param List<FlightViewModel.FlightListResponse.ResponseData>?     */    private fun setDataToRecyclerViewAdapterNextPage(        responseData: List<FlightViewModel.FlightListResponse.ResponseData>?    ) {        adapter!!.removeLoadingFooter()        isLoadingOn = false        adapter!!.addAll(responseData!!)        if (!isLastPg)            adapter!!.addLoadingFooter()    }    /**     * This method is to set data from intent and initialise listeners     */    private fun setDataAndListeners(v: View) {        recyclerView = v.findViewById(R.id.recycler_view_one_way) as RecyclerView        val layoutManager = LinearLayoutManager(context)        recyclerView!!.layoutManager = layoutManager as RecyclerView.LayoutManager?        //scroller for pagination        recyclerView!!.addOnScrollListener(object : PaginationScrollListener(layoutManager) {            override fun loadMoreItems() {                isLoadingOn = true                currentPage += 1                if (!isLastPg)                    loadNextPage()                else                    isLoadingOn = false            }            override fun isLastPage(): Boolean {                return isLastPg            }            override fun isLoading(): Boolean {                return isLoadingOn            }            override fun getTotalPageCount(): Int {                return TOTAL_PAGES            }        })        //sort click        btn_sort.setOnClickListener {            if (dataListAll?.isNotEmpty()!!)                sortBy()        }        //departure click        tv_departure.text = Utility.getCurrentDateNow()        tv_departure.setOnClickListener {            //Utility.dateDialogWithMinMaxDate(c, activity, tv_departure, 0)            openCalenderActivityFlightOneWay(                javaClass.simpleName.toString(),                "DepartureClick", CALENDER_SELECTION_REQUEST            )        }        tv_return.visibility = View.GONE        //travel class select click        tv_class.setOnClickListener { selectTravelClass() }        tv_class.text = ", " + travelClass        btn_passengers.text =            showPassengersAdult(adults).toString() + showPassengersChildren(childrens).toString() +                    showPassengersInfants(infants).toString()        btn_passengers.setOnClickListener { selectTravelClass() }        //search click        im_btn_search.setOnClickListener {            callAttemptSearchWithoutFilter()        }        //filter click        btn_filter.setOnClickListener {            callAttemptSearchWithFilter()        }        //from click        edt_from.setOnClickListener {            Utility.hideSoftKeyboard(v)            openSearchActivityFlightReturn(                this.javaClass.simpleName.toString(),                FROM_SELECTION_REQUEST,                Constant.Path.FROM            )        }        //to click        edt_to.setOnClickListener {            Utility.hideSoftKeyboard(v)            openSearchActivityFlightReturn(                this.javaClass.simpleName.toString(),                TO_SELECTION_REQUEST,                Constant.Path.TO            )        }    }    private fun openCalenderActivityFlightOneWay(        title: String, action: String,        selectionRequest: Int    ) {        val intent = Intent(context, CalenderActivity::class.java)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)        intent.putExtra(Constant.Path.ACTION_TITLE, action)        intent.putExtra(Constant.Path.SELECTED_FROM_DATE, tv_departure.text.toString())        intent.putExtra(Constant.Path.SELECTED_TO_DATE, tv_return.text.toString())        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)        startActivityForResult(intent, selectionRequest)    }    private fun openFilter() {        val intent = Intent(context, FilterActivity::class.java)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, "FragmentOneWay")        intent.putExtra(Constant.Path.FILTER_DATA, filterData)        intent.putExtra(Constant.Path.MAX_PRICE, maxPrice)        intent.putExtra(Constant.Path.MIN_PRICE, minPrice)        startActivityForResult(intent, Filter_SELECTION_REQUEST)    }    override fun onFlightRowClick(        responseData: FlightViewModel.FlightListResponse.ResponseData,        totalPassengers: String?    ) {        enterNextFragment(responseData)    }    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        super.onActivityResult(requestCode, resultCode, data)        when (requestCode) {            FROM_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    isItemClicked = true                    fromCode = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CODE)                    fromCity = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CITY)                    if (fromCode.equals(toCode)) {                        edt_from.setText("")                        fromCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else if (fromCity.equals(toCity)) {                        edt_from.setText("")                        fromCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else {                        edt_from.setText(                            data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_NAME)                        )                    }                }            }            TO_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    isItemClickedTo = true                    toCode = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CODE)                    toCity = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CITY)                    if (fromCode.equals(toCode)) {                        edt_to.setText("")                        toCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else if (fromCity.equals(toCity)) {                        edt_to.setText("")                        toCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else {                        edt_to.setText(data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_NAME))                    }                }            }            Filter_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    filterData = data?.getParcelableExtra(Constant.Path.FILTER_DATA)                    currentPage = 0                    isFiltering = true                    callFlightDetailsApi(filterData, isFiltering)                }            }            CALENDER_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    selectedFromDt = data?.getStringExtra(Constant.Path.RETURN_SELECTED_DEP_DATE)                    selectedToDt = data?.getStringExtra(Constant.Path.RETURN_SELECTED_RET_DATE)                    tv_departure.text = selectedFromDt                    tv_return.setText(selectedToDt)                }            }        }    }    override fun onPause() {        super.onPause()        edt_from.error = null        edt_to.error = null    }    private fun setDataToRecyclerViewAdapter(        responseData: List<FlightViewModel.FlightListResponse.ResponseData>?    ) {        dataListAll?.clear()        if (responseData != null) {            if (Pref.getValue(context, PrefConstants.USER_ID, "") != "0")                Pref.getValue(context, PrefConstants.CURRENCY, "")            else                currency = responseData[0].currency            totalPassengers =                showPassengersAdult(adults).toString() + showPassengersChildren(childrens).toString() +                        showPassengersInfants(infants).toString()            adapter =                RecyclerViewAdapter(ArrayList(), this, totalPassengers, this.javaClass.simpleName.toString(), this)            recyclerView!!.adapter = adapter            dataListAll!!.addAll(responseData)            loadFirstPage(dataListAll)        }    }    private fun loadFirstPage(dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>?) {        isLoadingOn = false        adapter!!.addAll(dataListAll!!)        if (!isLastPg)            adapter!!.addLoadingFooter()        else            adapter!!.removeLoadingFooter()    }    private fun enterNextFragment(responseData: FlightViewModel.FlightListResponse.ResponseData) {        val intent = Intent(activity?.baseContext, TripDetailsActivity::class.java)        intent.putExtra(Constant.Path.FLIGHT_DETAILS, responseData)        intent.putExtra(Constant.Path.PRICE, responseData.price?.toString())        intent.putExtra(Constant.Path.TOTAL_PASSENGERS, totalPassengers)        intent.putExtra(Constant.Path.CABIN_CLASS, travelClass)        startActivity(intent)    }    private var adults: String? = "1"    private var childrens: String? = "0"    private var infants: String? = "0"    private fun selectTravelClass() {        val dialogBuilder = AlertDialog.Builder(this.context!!).create()        val inflater = this.layoutInflater        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)        dialogView.ll_passenger_info.visibility = View.VISIBLE        dialogView.ll_apply.visibility = View.VISIBLE        dialogView.tv_dialog_header.text = getString(R.string.passenger_information)        dialogView.tv_dialog_header_rcv.text = getString(R.string.cabin_class)        dialogView.tv_quantity_adult.text = adults        dialogView.tv_quantity_children.text = childrens        dialogView.tv_quantity_infants.text = infants        dialogView.imv_minus_adult.setOnClickListener {            Utility.onMinusClick(dialogView.tv_quantity_adult, true, false, dialogView.tv_quantity_infants)        }        dialogView.imv_plus_adult.setOnClickListener {            if (dialogView.tv_quantity_adult.text?.toString()?.toInt()!!.plus(dialogView.tv_quantity_children.text?.toString()?.toInt()!!).plus(                    dialogView.tv_quantity_infants.text?.toString()?.toInt()!!                ) < 9            )                Utility.onAddClick(dialogView.tv_quantity_adult, true, false, "")        }        dialogView.imv_minus_children.setOnClickListener {            Utility.onMinusClick(dialogView.tv_quantity_children, false, false, null)        }        dialogView.imv_plus_children.setOnClickListener {            if (dialogView.tv_quantity_adult.text?.toString()?.toInt()!!.plus(dialogView.tv_quantity_children.text?.toString()?.toInt()!!).plus(                    dialogView.tv_quantity_infants.text?.toString()?.toInt()!!                ) < 9            )                Utility.onAddClick(dialogView.tv_quantity_children, false, false, "")        }        dialogView.imv_minus_infants.setOnClickListener {            Utility.onMinusClick(dialogView.tv_quantity_infants, false, true, null)        }        dialogView.imv_plus_infants.setOnClickListener {            if (dialogView.tv_quantity_adult.text?.toString()?.toInt()!!.plus(dialogView.tv_quantity_children.text?.toString()?.toInt()!!).plus(                    dialogView.tv_quantity_infants.text?.toString()?.toInt()!!                ) < 9            )                Utility.onAddClick(                    dialogView.tv_quantity_infants,                    false,                    true,                    dialogView.tv_quantity_adult.text.toString()                )        }        dialogView.button_dialog_apply.setOnClickListener {            adults = dialogView.tv_quantity_adult.text.toString()            childrens = dialogView.tv_quantity_children.text.toString()            infants = dialogView.tv_quantity_infants.text.toString()            btn_passengers.text =                showPassengersAdult(dialogView.tv_quantity_adult.text.toString()).toString() + showPassengersChildren(                    dialogView.tv_quantity_children.text.toString()                ).toString() +                        showPassengersInfants(dialogView.tv_quantity_infants.text.toString()).toString()            when {                dataListTravelClass!![0].isSelected -> travelClass = getString(R.string.economy_text)                dataListTravelClass!![1].isSelected -> travelClass =                    getString(R.string.pre_economy_text)                dataListTravelClass!![2].isSelected -> travelClass = getString(R.string.business_text)                dataListTravelClass!![3].isSelected -> travelClass = getString(R.string.first_text)            }            tv_class.text = ", " + travelClass            callAttemptSearchWithoutFilter()            dialogBuilder.dismiss()        }        dialogView.button_dialog_cancel.setOnClickListener {            dataListTravelClass!![adapterPosition].isSelected = false            when {                tv_class.text.toString().removePrefix(", ") == getString(R.string.economy_text) -> dataListTravelClass!![0].isSelected =                    true                tv_class.text.toString().removePrefix(", ") == getString(R.string.pre_economy_text) -> dataListTravelClass!![1].isSelected =                    true                tv_class.text.toString().removePrefix(", ") == getString(R.string.business_text) -> dataListTravelClass!![2].isSelected =                    true                tv_class.text.toString().removePrefix(", ") == getString(R.string.first_text) -> dataListTravelClass!![3].isSelected =                    true            }            dialogBuilder.dismiss()        }        val layoutManager = LinearLayoutManager(context)        dialogView.selection_list?.layoutManager = layoutManager        val adapter = SignleSelectionAdapter(            this.context!!,            dataListTravelClass!!, true,            object : SignleSelectionAdapter.OnClickListener {                override fun onListItemClick(                    dataList: ArrayList<CommonSelectorPojo>,                    position: Int                ) {                    adapterPosition = position                    travelClass = dataList[position].itemsName                }            })        dialogView.selection_list.adapter = adapter        dialogBuilder.setView(dialogView)        dialogBuilder.show()    }    private fun callAttemptSearchWithoutFilter() {        filterData = null        attemptSearch(0)    }    private fun callAttemptSearchWithFilter() {        attemptSearch(1)    }    private var adapterPosition: Int = 0    private var code: String? = "ECONOMY"    private fun sortBy() {        val dialogBuilder = AlertDialog.Builder(this.context!!).create()        val inflater = this.layoutInflater        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)        dialogView.tv_dialog_header.text = getString(R.string.sort_by)        val layoutManager = LinearLayoutManager(context)        dialogView.selection_list?.layoutManager = layoutManager        val adapter = SignleSelectionSortAdapter(            this.context!!,            dataListSortBy!!, true,            object : SignleSelectionSortAdapter.OnClickListener {                override fun onListItemClick(                    dataList: ArrayList<CommonSelectorPojo>,                    position: Int                ) {                    sortByCode = dataList[position].code                    sortByOneWay(sortByCode)                    dialogBuilder.dismiss()                }            })        dialogView.selection_list.adapter = adapter        dialogBuilder.setView(dialogView)        dialogBuilder.setCanceledOnTouchOutside(true)        dialogBuilder.show()    }    private fun sortByOneWay(sortByCode: String?) {        if (null != sortByCode) {            when (sortByCode) {                resources.getString(R.string.price_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c].totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].totalDuration).toString()                    }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    sortedList?.sortBy { it.price?.toDouble() }                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.duration_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c].totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].totalDuration).toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    for (c in sortedList?.indices!!) {                        Log.d("duration--", sortedList[c].totalDurationFormatted + "  " + sortedList[c].price)                    }                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.outbound_takeoff_time_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c].totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].outbondFlightDetails?.sortingStartTime)                                .toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.outbound_landing_time_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c].totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].outbondFlightDetails?.sortingEndTime)                                .toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    setDataToRecyclerViewAdapter(sortedList)                }            }        }    }    private fun openSearchActivityFlightReturn(        title: String,        selectionRequest: Int,        flag: String    ) {        val intent = Intent(context, AirportsSearchActivity::class.java)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)        intent.putExtra(Constant.Path.FLAG, flag)        startActivityForResult(intent, selectionRequest)    }    private fun attemptSearch(openFilter: Int) {        // Reset errors.        edt_from.error = null        edt_to.error = null        val edtFrom = edt_from.text.toString()        val edtTo = edt_to.text.toString()        val departureDate = tv_departure.text.toString()        var cancel = false        var focusView: View? = null        //ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST        if (TextUtils.isEmpty(edtFrom)) {            edt_from.error = getString(R.string.error_field_required_from_airport)            focusView = edt_from            cancel = true        } else if (TextUtils.isEmpty(edtTo)) {            edt_to.error = getString(R.string.error_field_required_to_airport)            focusView = edt_to            cancel = true        } else if (TextUtils.isEmpty(departureDate)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_required_departure),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            focusView = tv_departure            cancel = true        } else if (fromCode.equals(toCode)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_same_location),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            cancel = true        } else if (fromCity.equals(toCity)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_same_location),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            cancel = true        }        if (cancel) {            focusView?.requestFocus()        } else {            if (openFilter != 1) {                isSerachClicked = true                currentPage = 0                dataListAll!!.clear()                adapter!!.clear()                isFiltering = false                callFlightDetailsApi(filterData, isFiltering)            } else {                if (isSerachClicked)                    openFilter()            }        }    }    private fun callFlightDetailsApi(        filterData: FilterModel.Filter?,        isFiltering: Boolean    ) {        if (Pref.getValue(context, PrefConstants.CURRENCY, "")!!.equals(""))            currency = Pref.getValue(context, PrefConstants.CURRENCY_DEFAULT, "GB")        else            currency = Pref.getValue(context, PrefConstants.CURRENCY, "")!!        if (currency.equals("GB"))            currency = "GBP"        when {            dataListTravelClass!![0].isSelected -> travelClassCode = getString(R.string.economy_code)            dataListTravelClass!![1].isSelected -> travelClassCode =                getString(R.string.pre_economy_code)            dataListTravelClass!![2].isSelected -> travelClassCode = getString(R.string.business_code)            dataListTravelClass!![3].isSelected -> travelClassCode = getString(R.string.first_code)        }        presenter.callFlightDetails(            Pref.getValue(context, PrefConstants.USER_ID, "0").toString(),            adults!!,            travelClassCode!!,//ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST            childrens!!,            tv_departure.text.toString(),            getString(R.string.oneway),            fromCode.toString(),            toCode.toString(),            infants!!,            "en",            "",            currency!!,            filterData?.price_from.toString(),            filterData?.price_to.toString(),            filterData?.dtime_from.toString(),            filterData?.dtime_to.toString(),            filterData?.atime_from.toString(),            filterData?.atime_to.toString(),            filterData?.ret_dtime_from.toString(),            filterData?.ret_dtime_to.toString(),            filterData?.ret_atime_from.toString(),            filterData?.ret_atime_to.toString(),            filterData?.max_fly_duration.toString(),            filterData?.max_stopovers,            currentPage,            TOTAL_PAGES,            isFiltering        )    }    private fun init() {        this.model = FlightViewModel(context)        this.presenter = FlightPresenterImpl(this, context)        dataListSortBy = ArrayList()        dataListTravelClass = ArrayList()        //sort by        dataListSortBy?.add(            CommonSelectorPojo(                "1",                getString(R.string.price_text),                getString(R.string.price_code),                true            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "2",                getString(R.string.duration_text),                getString(R.string.duration_code),                false            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "3",                getString(R.string.outbound_takeoff_time),                getString(R.string.outbound_takeoff_time_code),                false            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "4",                getString(R.string.outbound_landing_time),                getString(R.string.outbound_landing_time_code),                false            )        )        //travel class //ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST        dataListTravelClass?.add(            CommonSelectorPojo(                "1",                getString(R.string.economy_text),                getString(R.string.economy_code),                true            )        )        dataListTravelClass?.add(            CommonSelectorPojo(                "2",                getString(R.string.pre_economy_text),                getString(R.string.pre_economy_code),                false            )        )        dataListTravelClass?.add(            CommonSelectorPojo(                "3",                getString(R.string.business_text),                getString(R.string.business_code),                false            )        )        dataListTravelClass?.add(            CommonSelectorPojo(                "4",                getString(R.string.first_text),                getString(R.string.first_code),                false            )        )    }    private fun showPassengersAdult(adults: String?): CharSequence? {        return if (!adults.equals("0"))            "$adults Adult "        else            ""    }    private fun showPassengersChildren(children: String?): CharSequence? {        return if (!children.equals("0"))            "$children Children "        else            ""    }    private fun showPassengersInfants(infants: String?): CharSequence? {        return if (!infants.equals("0"))            "$infants Infants "        else            ""    }}