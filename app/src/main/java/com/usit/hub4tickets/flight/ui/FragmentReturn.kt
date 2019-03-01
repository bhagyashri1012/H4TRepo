package com.usit.hub4tickets.flight.uiimport android.app.Activityimport android.content.Intentimport android.os.Bundleimport android.support.v7.app.AlertDialogimport android.support.v7.widget.LinearLayoutManagerimport android.support.v7.widget.RecyclerViewimport android.text.TextUtilsimport android.util.Logimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.common.RootFragmentimport com.usit.hub4tickets.domain.presentation.presenters.FlightPresenterimport com.usit.hub4tickets.domain.presentation.presenters.FlightPresenter.MainView.ViewState.*import com.usit.hub4tickets.domain.presentation.screens.main.FlightPresenterImplimport com.usit.hub4tickets.flight.adapter.RecyclerViewAdapterimport com.usit.hub4tickets.flight.model.FilterModelimport com.usit.hub4tickets.flight.model.FlightViewModelimport com.usit.hub4tickets.search.AirportsSearchActivityimport com.usit.hub4tickets.search.model.CommonSelectorPojoimport com.usit.hub4tickets.utils.*import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenterimport kotlinx.android.synthetic.main.fragment.*import kotlinx.android.synthetic.main.search_layout.*import kotlinx.android.synthetic.main.sort_by_dialog.view.*import java.util.*import kotlin.collections.ArrayList/** * Created by Bhagyashri Burade * Date: 24/10/2018 * Email: bhagyashri.burade@usit.net.in */class FragmentReturn : RootFragment(), RecyclerViewAdapter.OnItemClickListener, FlightPresenter.MainView {    val c = Calendar.getInstance()    private var isItemClicked: Boolean = false    private var isItemClickedTo: Boolean = false    private var fromCode: String? = null    private var toCode: String? = null    private var fromCity: String? = null    private var toCity: String? = null    private var sortByCode: String? = null    private var travelClassCode: String? = "ECONOMY"    private var travelClass: String? = "Economy"    private val FROM_SELECTION_REQUEST = 501    private val TO_SELECTION_REQUEST = 502    private val FILTER_SELECTION_REQUEST = 503    private val CALENDER_SELECTION_REQUEST = 504    private var recyclerView: RecyclerView? = null    private lateinit var model: FlightViewModel    private lateinit var presenter: FlightPresenter    private val dataListAll: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? = ArrayList()    var filterData: FilterModel.Filter? = null    private var mUserSeen = false    private var mViewCreated = false    private var maxPrice: String? = "0"    private var minPrice: String? = "0"    private var selectedFromDt: String? = ""    private var selectedToDt: String? = ""    var adapter: RecyclerViewAdapter? =        RecyclerViewAdapter(            items = emptyList(),            listener = null,            totalPassengers = null,            className = javaClass.simpleName.toString()        )    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        retainInstance = true    }    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {        return inflater.inflate(com.usit.hub4tickets.R.layout.fragment, container, false)    }    override fun setUserVisibleHint(isVisibleToUser: Boolean) {        super.setUserVisibleHint(isVisibleToUser)        if (!mUserSeen && isVisibleToUser) {            mUserSeen = true            onUserFirstSight()            tryViewCreatedFirstSight()        }        onUserVisibleChanged(isVisibleToUser)    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        // Override this if you want to get savedInstanceState.        mViewCreated = true        tryViewCreatedFirstSight()    }    override fun onDestroyView() {        super.onDestroyView()        mViewCreated = false        mUserSeen = false    }    private fun tryViewCreatedFirstSight() {        if (mUserSeen && mViewCreated) {            onViewCreatedFirstSight(view)        }    }    private fun onViewCreatedFirstSight(view: View?) {        init()        setDataAndListeners(view!!)    }    private fun onUserFirstSight() {}    private fun onUserVisibleChanged(visible: Boolean) {}    override fun doRetrieveModel(): FlightViewModel = this.model    override fun showState(viewState: FlightPresenter.MainView.ViewState) {        when (viewState) {            IDLE -> {                Utility.showProgress(false, context)                //Utility.showProgress(false, circularProgressBar)            }            LOADING -> {                Utility.showProgress(true, context)                //Utility.showProgress(true, circularProgressBar)            }            FLIGHT_DETAILS_SUCCESS -> {                Utility.showNoDataFound(false, rl_flight_not_found)                maxPrice = model.flightListViewModel.maxPrice                minPrice = model.flightListViewModel.minPrice                setDataToRecyclerViewAdapter(model.flightListViewModel.responseData as List<FlightViewModel.FlightListResponse.ResponseData>?)                sortByReturn(sortByCode)            }            FLIGHT_NOT_FOUND            -> {                dataListAll?.clear()                adapter?.notifyDataSetChanged()                Utility.showNoDataFound(true, rl_flight_not_found)            }            ERROR            -> {                if (dataListAll?.size != 0) {                    Utility.showNoDataFound(false, rl_flight_not_found)                }                Utility.showCustomDialog(context, doRetrieveModel().errorMessage.message, "", null)            }        }    }    override fun onFlightRowClick(        responseData: FlightViewModel.FlightListResponse.ResponseData,        totalPassengers: String?    ) {        enterNextFragment(responseData, totalPassengers!!)    }    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        super.onActivityResult(requestCode, resultCode, data)        when (requestCode) {            FROM_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    isItemClicked = true                    fromCode = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CODE)                    fromCity = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CITY)                    if (fromCode.equals(toCode)) {                        edt_from.setText("")                        fromCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else if (fromCity.equals(toCity)) {                        edt_from.setText("")                        fromCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else {                        edt_from.setText(                            data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_NAME)                        )                    }                }            }            TO_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    isItemClickedTo = true                    toCode = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CODE)                    toCity = data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_CITY)                    if (fromCode.equals(toCode)) {                        edt_to.setText("")                        toCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else if (fromCity.equals(toCity)) {                        edt_to.setText("")                        toCode = ""                        CustomDialogPresenter.showDialog(                            context,                            "",                            getString(R.string.error_field_same_location),                            context!!.resources.getString(                                R.string.ok                            ),                            null,                            null                        )                    } else {                        edt_to.setText(                            data?.getStringExtra(PrefConstants.SELECTED_AIRPORT_NAME)                        )                    }                }            }            FILTER_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    filterData = data?.getParcelableExtra(Constant.Path.FILTER_DATA)                    callFlightDetailsApi(filterData)                }            }            CALENDER_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    selectedFromDt = data?.getStringExtra(Constant.Path.RETURN_SELECTED_DEP_DATE)                    selectedToDt = data?.getStringExtra(Constant.Path.RETURN_SELECTED_RET_DATE)                    tv_departure.text = selectedFromDt                    tv_return.text = selectedToDt                }            }        }    }    override fun onPause() {        super.onPause()        edt_from.error = null//removes error        edt_to.error = null    }    private fun enterNextFragment(        responseData: FlightViewModel.FlightListResponse.ResponseData,        totalPassengers: String    ) {        val intent = Intent(activity?.baseContext, TripDetailsActivity::class.java)        intent.putExtra(Constant.Path.FLIGHT_DETAILS, responseData)        intent.putExtra(Constant.Path.PRICE, responseData.price?.toString())        intent.putExtra(Constant.Path.TOTAL_PASSENGERS, totalPassengers)        intent.putExtra(Constant.Path.CABIN_CLASS, travelClass)        startActivity(intent)    }    private fun openSearchActivityFlightReturn(        title: String,        selectionRequest: Int,        flag: String    ) {        val intent = Intent(context, AirportsSearchActivity::class.java)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)        intent.putExtra(Constant.Path.FLAG, flag)        startActivityForResult(intent, selectionRequest)    }    private fun openCalenderActivityFlightReturn(        title: String, action: String,        selectionRequest: Int    ) {        val intent = Intent(context, CalenderActivity::class.java)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)        intent.putExtra(Constant.Path.ACTION_TITLE, action)        intent.putExtra(Constant.Path.SELECTED_FROM_DATE, tv_departure.text.toString())        intent.putExtra(Constant.Path.SELECTED_TO_DATE, tv_return.text.toString())        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)        startActivityForResult(intent, selectionRequest)    }    private var isSerachClicked: Boolean = false    private fun attemptSearch(openFilter: Int) {        // Reset errors.        edt_from.error = null        edt_to.error = null        val edtFrom = edt_from.text.toString()        val edtTo = edt_to.text.toString()        val departureDate = tv_departure.text.toString()        val returnDate = tv_return.text.toString()        var cancel = false        var focusView: View? = null        if (TextUtils.isEmpty(edtFrom)) {            edt_from.error = getString(R.string.error_field_required_from_airport)            focusView = edt_from            cancel = true        } else if (TextUtils.isEmpty(edtTo)) {            edt_to.error = getString(R.string.error_field_required_to_airport)            focusView = edt_to            cancel = true        } else if (TextUtils.isEmpty(departureDate)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_required_departure),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            focusView = tv_departure            cancel = true        } else if (TextUtils.isEmpty(returnDate)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_required_return),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            focusView = tv_return            cancel = true        } else if (!Utility.validateDate(departureDate, returnDate)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_after_return),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            focusView = tv_departure            cancel = true        } else if (fromCode.equals(toCode)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_same_location),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            cancel = true        } else if (fromCity.equals(toCity)) {            CustomDialogPresenter.showDialog(                context,                "",                getString(R.string.error_field_same_location),                context!!.resources.getString(                    R.string.ok                ),                null,                null            )            cancel = true        }        if (cancel) {            focusView?.requestFocus()        } else {            if (openFilter != 1) {                isSerachClicked = true                callFlightDetailsApi(filterData)            } else {                if (isSerachClicked)                    openFilter()            }        }    }    private var currency: String? = ""    private fun callFlightDetailsApi(filterData: FilterModel.Filter?) {        if (Pref.getValue(context, PrefConstants.CURRENCY_ID, "")!!.equals(""))            currency = Pref.getValue(context, PrefConstants.CURRENCY_DEFAULT, "")        else            currency = ""        Log.d("return currency -", currency)        presenter.callFlightDetails(            Pref.getValue(context, PrefConstants.USER_ID, "0").toString(),            adults!!,            travelClassCode.toString(),//ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST            children!!,            tv_departure.text.toString(),            getString(R.string.flight_return),            fromCode.toString(),            toCode.toString(),            infants!!,            "en",            tv_return.text.toString(),            currency!!,            filterData?.price_from.toString(),            filterData?.price_to.toString(),            filterData?.dtime_from.toString(),            filterData?.dtime_to.toString(),            filterData?.atime_from.toString(),            filterData?.atime_to.toString(),            filterData?.ret_dtime_from.toString(),            filterData?.ret_dtime_to.toString(),            filterData?.ret_atime_from.toString(),            filterData?.ret_atime_to.toString(),            filterData?.max_fly_duration.toString(),            filterData?.max_stopovers        )    }    private fun sortByDailog() {        val dialogBuilder = AlertDialog.Builder(this.context!!).create()        val inflater = this.layoutInflater        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)        dialogView.tv_dialog_header.text = getString(R.string.sort_by)        val layoutManager = LinearLayoutManager(context)        dialogView.selection_list?.layoutManager = layoutManager        val adapter = SignleSelectionSortAdapter(            this.context!!,            dataListSortBy!!, true,            object : SignleSelectionSortAdapter.OnClickListener {                override fun onListItemClick(                    dataList: ArrayList<CommonSelectorPojo>,                    position: Int                ) {                    sortByCode = dataList[position].code                    sortByReturn(sortByCode)                    dialogBuilder.dismiss()                }            })        dialogView.selection_list.adapter = adapter        dialogBuilder.setView(dialogView)        dialogBuilder.setCanceledOnTouchOutside(true)        dialogBuilder.show()    }    private fun sortByReturn(sortByCode: String?) {        if (null != sortByCode) {            when (sortByCode) {                resources.getString(R.string.price_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c]?.totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].totalDuration).toString()                    }                    sortedList?.sortByDescending { it.totalDurationFormatted?.toInt() }                    sortedList?.sortBy { it.price?.toDouble() }                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.duration_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c]?.totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].totalDuration).toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.outbound_takeoff_time_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c]?.totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].outbondFlightDetails?.sortingStartTime)                                .toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    /*for (c in sortedList?.indices!!) {                        Log.d("takeoff--", sortedList[c].totalDurationFormatted + "  " + sortedList[c].price)                    }*/                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.outbound_landing_time_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c]?.totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].outbondFlightDetails?.sortingEndTime)                                .toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.inbound_takeoff_time_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c]?.totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].inbondFlightDetails?.sortingStartTime)                                .toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    setDataToRecyclerViewAdapter(sortedList)                }                resources.getString(R.string.inbound_landing_time_code) -> {                    val sortedList: ArrayList<FlightViewModel.FlightListResponse.ResponseData>? =                        ArrayList()                    sortedList?.addAll(dataListAll!!)                    for (c in dataListAll?.indices!!) {                        sortedList!![c]?.totalDurationFormatted =                            Utility.getDurationFromString(dataListAll[c].inbondFlightDetails?.sortingEndTime)                                .toString()                    }                    sortedList?.sortBy { it.price?.toDouble() }                    sortedList?.sortBy { it.totalDurationFormatted?.toInt() }                    /* for (c in sortedList?.indices!!) {                         Log.d("inbound landin--", sortedList[c].totalDurationFormatted + "  " + sortedList[c].price)                     }*/                    setDataToRecyclerViewAdapter(sortedList)                }            }        }    }    private var adults: String? = "1"    private var children: String? = "0"    private var infants: String? = "0"    private var adapterPosition: Int = 0    private var adapterApplyPosition: Int = 0    private fun selectTravelClass() {        val dialogBuilder = AlertDialog.Builder(this.context!!).create()        val inflater = this.layoutInflater        val dialogView = inflater.inflate(R.layout.sort_by_dialog, null)        dialogView.ll_passenger_info.visibility = View.VISIBLE        dialogView.ll_apply.visibility = View.VISIBLE        dialogView.tv_dialog_header.text = getString(R.string.passenger_information)        dialogView.tv_dialog_header_rcv.text = getString(R.string.cabin_class)        dialogView.tv_quantity_adult.text = adults        dialogView.tv_quantity_children.text = children        dialogView.tv_quantity_infants.text = infants        dialogView.imv_minus_adult.setOnClickListener {            Utility.onMinusClick(dialogView.tv_quantity_adult, true, false, dialogView.tv_quantity_infants)        }        dialogView.imv_plus_adult.setOnClickListener {            if (dialogView.tv_quantity_adult.text?.toString()?.toInt()!!.plus(dialogView.tv_quantity_children.text?.toString()?.toInt()!!).plus(                    dialogView.tv_quantity_infants.text?.toString()?.toInt()!!                ) < 9            )                Utility.onAddClick(dialogView.tv_quantity_adult, true, false, "")        }        dialogView.imv_minus_children.setOnClickListener {            Utility.onMinusClick(dialogView.tv_quantity_children, false, false, null)        }        dialogView.imv_plus_children.setOnClickListener {            if (dialogView.tv_quantity_adult.text?.toString()?.toInt()!!.plus(dialogView.tv_quantity_children.text?.toString()?.toInt()!!).plus(                    dialogView.tv_quantity_infants.text?.toString()?.toInt()!!                ) < 9            )                Utility.onAddClick(dialogView.tv_quantity_children, false, false, "")        }        dialogView.imv_minus_infants.setOnClickListener {            Utility.onMinusClick(                dialogView.tv_quantity_infants,                false,                true,                null            )        }        dialogView.imv_plus_infants.setOnClickListener {            if (dialogView.tv_quantity_adult.text?.toString()?.toInt()!!.plus(dialogView.tv_quantity_children.text?.toString()?.toInt()!!).plus(                    dialogView.tv_quantity_infants.text?.toString()?.toInt()!!                ) < 9            )                Utility.onAddClick(                    dialogView.tv_quantity_infants,                    false,                    true,                    dialogView.tv_quantity_adult.text.toString()                )        }        dialogView.button_dialog_apply.setOnClickListener {            adults = dialogView.tv_quantity_adult.text.toString()            children = dialogView.tv_quantity_children.text.toString()            infants = dialogView.tv_quantity_infants.text.toString()            btn_passengers.text =                showPassengersAdult(dialogView.tv_quantity_adult.text.toString()).toString() + showPassengersChildren(                    dialogView.tv_quantity_children.text.toString()                ).toString() +                        showPassengersInfants(dialogView.tv_quantity_infants.text.toString()).toString()            tv_class.text = ", " + travelClass            attemptSearch(0)            dialogBuilder.dismiss()        }        dataListTravelClass!!.get(adapterPosition).isSelected = true        val layoutManager = LinearLayoutManager(context)        dialogView.selection_list?.layoutManager = layoutManager        val adapter = SignleSelectionAdapter(            this.context!!,            dataListTravelClass!!, true,            object : SignleSelectionAdapter.OnClickListener {                override fun onListItemClick(                    dataList: ArrayList<CommonSelectorPojo>,                    position: Int                ) {                    adapterPosition = position                    travelClassCode = dataList[position].code                    travelClass = dataList[position].itemsName                }            })        dialogView.button_dialog_cancel.setOnClickListener {            dataListTravelClass!!.get(adapterPosition).isSelected = false            adapter.notifyDataSetChanged()            dialogBuilder.dismiss()        }        dialogView.selection_list.adapter = adapter        dialogBuilder.setView(dialogView)        dialogBuilder.show()    }    private var dataListSortBy: ArrayList<CommonSelectorPojo>? = null    private var dataListTravelClass: ArrayList<CommonSelectorPojo>? = null    private fun init() {        this.model = FlightViewModel(context)        this.presenter = FlightPresenterImpl(this, context)        dataListSortBy = ArrayList()        dataListTravelClass = ArrayList()        //sort by        dataListSortBy?.add(            CommonSelectorPojo(                "1",                getString(R.string.price_text),                getString(R.string.price_code),                true            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "2",                getString(R.string.duration_text),                getString(R.string.duration_code),                false            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "3",                getString(R.string.outbound_takeoff_time),                getString(R.string.outbound_takeoff_time_code),                false            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "4",                getString(R.string.outbound_landing_time),                getString(R.string.outbound_landing_time_code),                false            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "5", getString(R.string.inbound_takeoff_time),                getString(R.string.inbound_takeoff_time_code),                false            )        )        dataListSortBy?.add(            CommonSelectorPojo(                "6", getString(R.string.inbound_landing_time),                getString(R.string.inbound_landing_time_code),                false            )        )        //travel class //ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST        dataListTravelClass?.add(            CommonSelectorPojo(                "1",                getString(R.string.economy_text),                getString(R.string.economy_code),                true            )        )        dataListTravelClass?.add(            CommonSelectorPojo(                "2",                getString(R.string.pre_economy_text),                getString(R.string.pre_economy_code),                false            )        )        dataListTravelClass?.add(            CommonSelectorPojo(                "3",                getString(R.string.business_text),                getString(R.string.business_code),                false            )        )        dataListTravelClass?.add(            CommonSelectorPojo(                "4",                getString(R.string.first_text),                getString(R.string.first_code),                false            )        )    }    private fun setDataAndListeners(v: View) {        tv_departure.text = Utility.getCurrentDateNow()        //  tv_return.text = Utility.getCurrentDateNow()        recyclerView = v?.findViewById(R.id.recycler_view) as RecyclerView        val layoutManager = LinearLayoutManager(context)        recyclerView!!.layoutManager = layoutManager as RecyclerView.LayoutManager?        btn_sort.setOnClickListener {            if (dataListAll?.isNotEmpty()!!)                sortByDailog()        }        tv_departure.setOnClickListener {            //Utility.dateDialogWithMinMaxDate(c, activity, tv_departure, 0)            openCalenderActivityFlightReturn(                javaClass.simpleName.toString(),                "DepartureClick", CALENDER_SELECTION_REQUEST            )        }        tv_return.setOnClickListener {            // Utility.dateDialogWithMinMaxDate(c, activity, tv_return, 0)            openCalenderActivityFlightReturn(                javaClass.simpleName.toString(), "ReturnClick",                CALENDER_SELECTION_REQUEST            )        }        tv_class.setOnClickListener { selectTravelClass() }        tv_class.text = ", " + travelClass        btn_passengers.text = showPassengersAdult(adults).toString() + showPassengersChildren(children).toString() +                showPassengersInfants(infants).toString()        btn_passengers.setOnClickListener { selectTravelClass() }        im_btn_search.setOnClickListener {            filterData = null            attemptSearch(0)        }        btn_filter.setOnClickListener {            attemptSearch(1)        }        edt_from.setOnClickListener {            Utility.hideSoftKeyboard(v)            openSearchActivityFlightReturn(                this.javaClass.simpleName.toString(),                FROM_SELECTION_REQUEST,                Constant.Path.FROM            )        }        edt_to.setOnClickListener {            //if (MainApplication.getInstance.isConnected()) {            Utility.hideSoftKeyboard(v)            openSearchActivityFlightReturn(                this.javaClass.simpleName.toString(),                TO_SELECTION_REQUEST,                Constant.Path.TO            )            //  } else {            //      doRetrieveModel().errorMessage.message =            //         doRetrieveModel().context!!.getString(R.string.message_no_internet)            //      showState(ERROR)            //   }        }        //frameLayout = parentFragment?.view?.findViewById(R.id.frame_layout) as FrameLayout    }    private fun openFilter() {        val intent = Intent(context, FilterActivity::class.java)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, "FragmentReturn")        intent.putExtra(Constant.Path.FILTER_DATA, filterData)        intent.putExtra(Constant.Path.MAX_PRICE, maxPrice)        intent.putExtra(Constant.Path.MIN_PRICE, minPrice)        startActivityForResult(intent, FILTER_SELECTION_REQUEST)    }    private var totalPassengers: String = ""    private fun setDataToRecyclerViewAdapter(responseData: List<FlightViewModel.FlightListResponse.ResponseData>?) {        dataListAll?.clear()        if (responseData != null) {            totalPassengers = showPassengersAdult(adults).toString() + showPassengersChildren(children).toString() +                    showPassengersInfants(infants).toString()            dataListAll?.addAll(responseData)        }        adapter = RecyclerViewAdapter(dataListAll!!, this, totalPassengers, javaClass.simpleName.toString())        recyclerView!!.adapter = adapter    }    private fun showPassengersAdult(adults: String?): CharSequence? {        return if (!adults.equals("0"))            "$adults Adult "        else            ""    }    private fun showPassengersChildren(children: String?): CharSequence? {        return if (!children.equals("0"))            "$children Children "        else            ""    }    private fun showPassengersInfants(infants: String?): CharSequence? {        return if (!infants.equals("0"))            "$infants Infants "        else            ""    }    override fun onBackPressed(): Boolean {        return fragmentManager?.popBackStackImmediate()!!    }}