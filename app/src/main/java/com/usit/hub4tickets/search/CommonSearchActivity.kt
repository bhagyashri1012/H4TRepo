package com.usit.hub4tickets.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.model.DashboardViewModel
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.search.model.CommonSelectorPojo
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_comman_serach.*

class CommonSearchActivity : BaseActivity() {

    private var searchItemsListAdapter: CommonSearchAdapter? = null
    private var strActivityTitle: String? = ""
    private var arrayListCommonSelectorInitial: ArrayList<DashboardViewModel.CountriesResponse.CountriesResponseData> =
        ArrayList()
    private var arrayListCommonSelectorLangInitial: ArrayList<DashboardViewModel.LanguageResponse.ResponseData> =
        ArrayList()
    private var arrayListCommonSelectorCurrencyInitial: ArrayList<DashboardViewModel.CurrencyResponse.CurrencyData> =
        ArrayList()
    private var arrayListCommonSelectorStateInitial: ArrayList<DashboardViewModel.StateResponse.ResponseData> =
        ArrayList()
    private var arrayListCommonSelectorCityInitial: ArrayList<DashboardViewModel.CityResponse.ResponseData> =
        ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comman_serach)
        Utility.hideKeyBordActivity(this)
        if (intent.extras != null) {
            strActivityTitle = intent.extras!!.getString(Constant.Path.ACTIVITY_TITLE)
            when (strActivityTitle) {
                "SettingsFragment" -> {
                    if (null != intent.extras.getSerializable(Constant.Path.LOCATION_LIST))
                        arrayListCommonSelectorInitial =
                                intent.extras.getSerializable(Constant.Path.LOCATION_LIST) as ArrayList<DashboardViewModel.CountriesResponse.CountriesResponseData>
                    if (null != intent.extras.getSerializable(Constant.Path.LANGUAGE_LIST))
                        arrayListCommonSelectorLangInitial =
                                intent.extras.getSerializable(Constant.Path.LANGUAGE_LIST) as ArrayList<DashboardViewModel.LanguageResponse.ResponseData>
                    if (null != intent.extras.getSerializable(Constant.Path.CURRENCY_LIST))
                        arrayListCommonSelectorCurrencyInitial =
                                intent.extras.getSerializable(Constant.Path.CURRENCY_LIST) as ArrayList<DashboardViewModel.CurrencyResponse.CurrencyData>
                }
                "PersonalInfoActivity" -> {
                    if (null != intent.extras.getSerializable(Constant.Path.LOCATION_LIST_PROFILE))
                        arrayListCommonSelectorInitial =
                                intent.extras.getSerializable(Constant.Path.LOCATION_LIST_PROFILE) as ArrayList<DashboardViewModel.CountriesResponse.CountriesResponseData>
                    if (null != intent.extras.getSerializable(Constant.Path.STATE_LIST_PROFILE))
                        arrayListCommonSelectorStateInitial =
                                intent.extras.getSerializable(Constant.Path.STATE_LIST_PROFILE) as ArrayList<DashboardViewModel.StateResponse.ResponseData>
                    if (null != intent.extras.getSerializable(Constant.Path.CITY_LIST_PROFILE))
                        arrayListCommonSelectorCityInitial =
                                intent.extras.getSerializable(Constant.Path.CITY_LIST_PROFILE) as ArrayList<DashboardViewModel.CityResponse.ResponseData>
                    if (null != intent.extras.getSerializable(Constant.Path.LANGUAGE_LIST_PROFILE))
                        arrayListCommonSelectorLangInitial =
                                intent.extras.getSerializable(Constant.Path.LANGUAGE_LIST_PROFILE) as ArrayList<DashboardViewModel.LanguageResponse.ResponseData>
                }

            }
        }
        initView()
    }

    private fun initView() {
        val arrayListCommonSelector: ArrayList<CommonSelectorPojo> = ArrayList()
        if (arrayListCommonSelectorInitial != null) {
            for (i in arrayListCommonSelectorInitial!!.indices) {
                arrayListCommonSelector.add(
                    i, CommonSelectorPojo(
                        arrayListCommonSelectorInitial[i].countryId.toString(),
                        arrayListCommonSelectorInitial[i].countryName,
                        arrayListCommonSelectorInitial[i].countryCode
                    )
                )

            }
        }

        if (arrayListCommonSelectorLangInitial != null) {
            for (i in arrayListCommonSelectorLangInitial!!.indices) {
                arrayListCommonSelector.add(
                    i, CommonSelectorPojo(
                        arrayListCommonSelectorLangInitial[i].languageId.toString(),
                        arrayListCommonSelectorLangInitial[i].name,
                        arrayListCommonSelectorLangInitial[i].languageCode
                    )
                )

            }
        }
        if (arrayListCommonSelectorCurrencyInitial != null) {
            for (i in arrayListCommonSelectorCurrencyInitial!!.indices) {
                arrayListCommonSelector.add(
                    i, CommonSelectorPojo(
                        arrayListCommonSelectorCurrencyInitial[i].currencyId,
                        arrayListCommonSelectorCurrencyInitial[i].name,
                        arrayListCommonSelectorCurrencyInitial[i].code
                    )
                )

            }
        }

        if (arrayListCommonSelectorStateInitial != null) {
            for (i in arrayListCommonSelectorStateInitial!!.indices) {
                arrayListCommonSelector.add(
                    i, CommonSelectorPojo(
                        arrayListCommonSelectorStateInitial[i].stateId,
                        arrayListCommonSelectorStateInitial[i].stateName,
                        ""
                    )
                )

            }
        }
        if (arrayListCommonSelectorCityInitial != null) {
            for (i in arrayListCommonSelectorCityInitial!!.indices) {
                arrayListCommonSelector.add(
                    i, CommonSelectorPojo(
                        arrayListCommonSelectorCityInitial[i].cityId,
                        arrayListCommonSelectorCityInitial[i].cityname,
                        arrayListCommonSelectorCityInitial[i].stateId
                    )
                )

            }
        }
        if (null != arrayListCommonSelector) {
            searchItemsListAdapter = CommonSearchAdapter(
                this,
                arrayListCommonSelector,
                object : CommonSearchAdapter.OnItemClickListener {
                    override fun onListItemClick(data: CommonSelectorPojo) {
                        Utility.hideKeyBordActivity(this@CommonSearchActivity)
                        val intent = Intent()
                        intent.putExtra(PrefConstants.SELECTED_ITEMS_ID, data.id)
                        intent.putExtra(PrefConstants.SELECTED_ITEMS_NAME, data.itemsName)
                        intent.putExtra(PrefConstants.SELECTED_ITEMS_TYPE, data.code)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }

                    override fun onNoData(isVisible: Boolean) {
                        if (isVisible) {
                            txt_no_data!!.visibility = View.VISIBLE
                        } else {
                            txt_no_data!!.visibility = View.GONE
                        }
                    }
                })

            val mLayoutManager = LinearLayoutManager(this)
            recycler_city_list!!.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
            recycler_city_list!!.adapter = searchItemsListAdapter

            var searchText = this.findViewById<SearchView>(R.id.search_text)
            searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.replace("\\s+$".toRegex(), "")
                    searchItemsListAdapter!!.getFilter().filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchItemsListAdapter!!.getFilter().filter(newText!!.trim { it <= ' ' })
                    return false
                }

            })
        }
    }

    override fun onBackPressed() {
        Utility.hideKeyBordActivity(this)
        super.onBackPressed()
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }
}