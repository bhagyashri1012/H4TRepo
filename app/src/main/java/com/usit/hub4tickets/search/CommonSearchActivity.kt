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
import kotlinx.android.synthetic.main.activity_select_city.*

class CommonSearchActivity : BaseActivity() {

    private var searchItemsListAdapter: CommonSearchAdapter? = null
    private var strActivityTitle: String? = ""


    private lateinit var arrayListCommonSelectorInitial: ArrayList<DashboardViewModel.CountriesResponse.ResponseData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_city)
        Utility.hideKeyBordActivity(this)
        if (intent.extras != null) {
            arrayListCommonSelectorInitial =
                    intent.extras.getSerializable(Constant.Path.LOCATION_LIST) as ArrayList<DashboardViewModel.CountriesResponse.ResponseData>
            strActivityTitle = intent.extras!!.getString(PrefConstants.ACTIVITY_TITLE)
        }
        search_text.isFocusable = false
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

            search_text.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.replace("\\s+$".toRegex(), "")
                    searchItemsListAdapter!!.filter(query!!)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchItemsListAdapter!!.filter(newText!!.trim { it <= ' ' })
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