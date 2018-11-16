package com.usit.hub4tickets.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.search.model.CommonSelectorPojo
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.activity_select_city.*

class CommonSearchActivity : BaseActivity() {

    private var searchItemsListAdapter: CommonSearchAdapter? = null
    private var strActivityTitle: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_city)
        Utility.hideKeyBordActivity(this)
        if (intent.extras != null) {
            // arrayListCommonSelector =
            intent.getSerializableExtra(PrefConstants.LOCATION_LIST) as ArrayList<CommonSelectorPojo>
            //strActivityTitle = intent.extras!!.getString(PrefConstants.ACTIVITY_TITLE)
        }
        initialiseToolbar()
        initView()
    }

    private fun initialiseToolbar() {
        search_items!!.isFocusable = false
    }

    private fun initView() {
        var arrayListCommonSelector: ArrayList<CommonSelectorPojo>? = null
        arrayListCommonSelector?.add(CommonSelectorPojo("1", "Pune", "City"))
        if (null != arrayListCommonSelector) {
            searchItemsListAdapter = CommonSearchAdapter(
                this,
                arrayListCommonSelector!!,
                object : CommonSearchAdapter.OnItemClickListener {
                    override fun onListItemClick(data: CommonSelectorPojo) {

                        Utility.hideKeyBordActivity(this@CommonSearchActivity)

                        val intent = Intent()
                        intent.putExtra(PrefConstants.SELECTED_ITEMS_ID, data.id)
                        intent.putExtra(PrefConstants.SELECTED_ITEMS_NAME, data.itemsName)
                        intent.putExtra(PrefConstants.SELECTED_ITEMS_TYPE, data.type)
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
            recycler_city_list!!.layoutManager = mLayoutManager
            recycler_city_list!!.adapter = searchItemsListAdapter

            search_items!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    query.replace("\\s+$".toRegex(), "")
                    searchItemsListAdapter!!.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {

                    searchItemsListAdapter!!.filter(newText.trim { it <= ' ' })
                    return false
                }

            })

        }
    }

    internal fun onBackButton() {
        Utility.hideKeyBordActivity(this)
        super.onBackPressed()
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }
}
