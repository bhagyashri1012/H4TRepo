package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FilterModel
import com.usit.hub4tickets.utils.Constant
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterActivity : AppCompatActivity() {
    private var minValuePrice: String = "0"
    private var maxValuePrice: String = "1000000"
    private var minValueDuration: String = "0"
    private var maxValueDuration: String = "0"
    private var minValueOutbound: String = "0"
    private var maxValueOutbound: String = "0"
    var filterData: FilterModel.Filter? = null

    private var activityTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_filter)

        setToolBar()
        setOtherListeners()
        if (intent.extras != null) {
            filterData = intent.getParcelableExtra(Constant.Path.FILTER_DATA)
            activityTitle = intent.getStringExtra(Constant.Path.ACTIVITY_TITLE)
            if (null != filterData)
                init()
        }
        if (activityTitle.equals("FragmentOneWay")) {
            ll_inbound.visibility = View.GONE

        }
        setRangeSeekbarForPrice()
        setRangeSeekbarForDuration()
        setRangeSeekbarForaOutstanding()
    }

    private fun init() {
        textMin_price.text = filterData?.price_from
        textMax_price.text = filterData?.price_to
        minValuePrice = filterData?.price_from.toString()
        maxValuePrice = filterData?.price_to.toString()
        range_seekbar_price.setMinValue(0f).setMaxValue(1000000f).setMinStartValue(minValuePrice.toFloat())
            .setMaxStartValue(maxValuePrice.toFloat()).apply()

        if (activityTitle.equals("FragmentOneWay")) {
            ll_inbound.visibility = View.GONE

        } else {
            ll_inbound.visibility = View.VISIBLE
            textMin.text = removePadValues(nullDefaultValues(filterData?.dtime_from, "0"))
            textMax.text = removePadValues(nullDefaultValues(filterData?.dtime_to, "100"))
            minValueDuration = removePadValues(filterData?.dtime_from.toString())
            maxValueDuration = removePadValues(filterData?.dtime_to.toString())
            range_seekbar_duration.setMinValue(0f).setMaxValue(24f)
                .setMinStartValue(minValueDuration.toFloat())
                .setMaxStartValue(zeroCheck(maxValueDuration).toFloat())
                .apply()
        }

        textMin_outbound.text = removePadValues(nullDefaultValues(filterData?.atime_from, "0"))
        textMax_outbound.text = removePadValues(nullDefaultValues(filterData?.atime_to, "100"))
        minValueOutbound = removePadValues(filterData?.atime_from.toString())
        maxValueOutbound = removePadValues(filterData?.atime_to.toString())
        range_seekbar_outbound_trip.setMinValue(0f).setMaxValue(24f)
            .setMinStartValue(minValueOutbound.toFloat())
            .setMaxStartValue(zeroCheck(maxValueOutbound).toFloat())
            .apply()

        switchButton_direct.isChecked = filterData?.max_stopovers?.contains(0)!!
        switchButton_1stop.isChecked = filterData?.max_stopovers?.contains(1)!!
        switchButton_2stops.isChecked = filterData?.max_stopovers?.contains(2)!!
    }

    private fun zeroCheck(maxDuration: String): String {
        if (maxDuration == "0:00")
            return "24"
        else if (maxDuration == "0")
            return "24"
        else if (maxDuration == "00:00")
            return "24"
        else if (maxDuration == "00")
            return "24"
        else
            return maxDuration
    }

    private fun nullDefaultValues(bundleData: String?, defaultValue: String): String {
        if (bundleData.equals("00:00"))
            return defaultValue
        else
            return bundleData.toString()
    }

    private fun removePadValues(value: String): String {
        if (null != value) {
            value.contains(":00")
            return value.removeSuffix(":00")
        } else
            return value
    }

    private fun setOtherListeners() {

        var maxStopovers: ArrayList<Int> = ArrayList()
        button_dialog_cancel.setOnClickListener { onBackPressed() }

        if (switchButton_direct.isChecked)
            maxStopovers.add(0)
        else
            maxStopovers.remove(0)

        if (switchButton_1stop.isChecked)
            maxStopovers.add(1)
        else
            maxStopovers.remove(1)
        if (switchButton_2stops.isChecked)
            maxStopovers.add(2)
        else
            maxStopovers.remove(2)

        switchButton_direct?.setOnCheckedChangeListener { _, isChecked ->
            maxStopovers.remove(0)
            if (isChecked)
                maxStopovers.add(0)
            else {
                if (!switchButton_1stop.isChecked && !switchButton_2stops.isChecked) {
                    maxStopovers.add(0)
                    switchButton_direct.isChecked = true

                } else
                    maxStopovers.remove(0)
            }
        }
        switchButton_1stop?.setOnCheckedChangeListener { _, isChecked ->
            maxStopovers.remove(1)
            if (isChecked)
                maxStopovers.add(1)
            else {
                if (!switchButton_direct.isChecked && !switchButton_2stops.isChecked) {
                    maxStopovers.add(1)
                    switchButton_1stop.isChecked = true

                } else
                    maxStopovers.remove(1)
            }
        }
        switchButton_2stops?.setOnCheckedChangeListener { _, isChecked ->
            maxStopovers.remove(2)

            if (isChecked)
                maxStopovers.add(2)
            else {
                if (!switchButton_direct.isChecked && !switchButton_1stop.isChecked) {
                    maxStopovers.add(2)
                    switchButton_2stops.isChecked = true
                } else
                    maxStopovers.remove(2)
            }
        }

        button_dialog_apply.setOnClickListener {
            if (activityTitle.equals("FragmentOneWay")) {
                val filterModel: FilterModel.Filter = FilterModel.Filter(
                    minValuePrice,
                    maxValuePrice,
                    "0:00",
                    "0:00",
                    minValueOutbound + ":00".padEnd(2),
                    maxValueOutbound + ":00".padEnd(2),
                    maxStopovers
                )
                val intent = Intent()
                intent.putExtra(Constant.Path.FILTER_DATA, filterModel)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else if (activityTitle.equals("FragmentReturn")) {
                val filterModel: FilterModel.Filter = FilterModel.Filter(
                    minValuePrice,
                    maxValuePrice,
                    minValueDuration + ":00".padEnd(2),
                    maxValueDuration + ":00".padEnd(2),
                    minValueOutbound + ":00".padEnd(2),
                    maxValueOutbound + ":00".padEnd(2),
                    maxStopovers
                )
                val intent = Intent()
                intent.putExtra(Constant.Path.FILTER_DATA, filterModel)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        }

    }

    private fun setToolBar() {
        titleToolBar?.text = resources.getString(com.usit.hub4tickets.R.string.filter)
        mainToolbar?.setNavigationOnClickListener { finish() }
    }

    private fun setRangeSeekbarForDuration() {
        range_seekbar_duration.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textMin.text = minValue.toString()
            textMax.text = maxValue.toString()
        }
        range_seekbar_duration.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            minValueDuration = minValue.toString()
            maxValueDuration = maxValue.toString()
        }
    }

    private fun setRangeSeekbarForPrice() {

        range_seekbar_price.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textMin_price.text = minValue.toString()
            textMax_price.text = maxValue.toString()
        }
        range_seekbar_price.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            minValuePrice = minValue.toString()
            maxValuePrice = maxValue.toString()
        }

    }

    private fun setRangeSeekbarForaOutstanding() {

        range_seekbar_outbound_trip.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textMin_outbound.text = minValue.toString()
            textMax_outbound.text = maxValue.toString()
        }

        range_seekbar_outbound_trip.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            /*Log.d(
                "CRS=>",
                minValue.toString() + " : " + maxValue.toString()
            )*/
            minValueOutbound = minValue.toString()
            maxValueOutbound = maxValue.toString()
        }
    }

    /*return fragmentManager?.popBackStackImmediate()!!
    else

        activity?.finish()
    return true*/

}