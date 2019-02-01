package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FilterModel
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Pref
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterActivity : AppCompatActivity() {
    private var minValuePrice: String = "0"
    private var maxValuePrice: String = "1000000"
    private var minValue: String = "0"
    private var maxValue: String = "0"
    private var minValuedef: String = "0"
    private var maxValuedef: String = "0"
    private var minValueLanding: String = "0"
    private var maxValueLanding: String = "0"
    private var minValueTakeOff: String = "0"
    private var maxValueTakeOff: String = "0"
    private var minValueInLanding: String = "0"
    private var maxValueInLanding: String = "0"
    private var minValueInTakeOff: String = "0"
    private var maxValueInTakeOff: String = "0"
    var filterData: FilterModel.Filter? = null

    private var activityTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_filter)

        setToolBar()
        setOtherListeners()
        if (intent.extras != null) {
            minValue = intent.getStringExtra(Constant.Path.MIN_PRICE)
            maxValue = intent.getStringExtra(Constant.Path.MAX_PRICE)
            filterData = intent.getParcelableExtra(Constant.Path.FILTER_DATA)
            activityTitle = intent.getStringExtra(Constant.Path.ACTIVITY_TITLE)
            if (null != filterData)
                init()
            else
                setDefaultValues()
        }
        if (activityTitle.equals("FragmentOneWay")) {
            ll_inbound.visibility = View.GONE
            setRangeSeekbarForPrice()
            setRangeSeekbarForObLanding()
            setRangeSeekbarObTakeOff()
        } else {
            setRangeSeekbarForPrice()
            setRangeSeekbarForObLanding()
            setRangeSeekbarObTakeOff()
            setRangeSeekbarIbTakeOff()
            setRangeSeekbarForIbLanding()
        }

    }

    private fun setDefaultValues() {
        textMin_price.text = minValue
        textMax_price.text = maxValue
        Pref.setValue(this, Constant.Path.DEF_MAX_PRICE, maxValue)
        Pref.setValue(this, Constant.Path.DEF_MIN_PRICE, minValue)
        range_seekbar_price.setMinValue(minValue.toFloat()).setMaxValue(maxValue.toFloat())
            .setMinStartValue(minValue.toFloat())
            .setMaxStartValue(maxValue.toFloat()).apply()
    }

    private fun init() {
        //textMin_price.text = filterData?.price_from
        //textMax_price.text = filterData?.price_to
        minValuedef = Pref.getValue(this, Constant.Path.DEF_MIN_PRICE, "0")!!
        maxValuedef = Pref.getValue(this, Constant.Path.DEF_MAX_PRICE, "0")!!
        minValuePrice = filterData?.price_from.toString()
        maxValuePrice = filterData?.price_to.toString()
        range_seekbar_price.setMinValue(minValuedef.toFloat()).setMaxValue(maxValuedef.toFloat())
            .setMinStartValue(minValuePrice.toFloat())
            .setMaxStartValue(maxValuePrice.toFloat()).apply()

        if (activityTitle.equals("FragmentOneWay")) {
            ll_inbound.visibility = View.GONE

        } else {
            ll_inbound.visibility = View.VISIBLE

        }
        textMin_outbound_takeofff.text = removePadValues(nullDefaultValues(filterData?.dtime_from, "0"))
        textMax_outbound_takeofff.text = removePadValues(nullDefaultValues(filterData?.dtime_to, "23:59"))
        minValueTakeOff = removePadValues(filterData?.dtime_from.toString())
        maxValueTakeOff = removePadValues(filterData?.dtime_to.toString())
        range_seekbar_outbound_trip.setMinValue(0f).setMaxValue(23.59f)
            .setMinStartValue(minValueTakeOff.toFloat())
            .setMaxStartValue(zeroCheck(maxValueTakeOff).toFloat())
            .apply()
        textMinOblanding.text = removePadValues(nullDefaultValues(filterData?.atime_from, "0"))
        textMaxOblanding.text = removePadValues(nullDefaultValues(filterData?.atime_to, "23:59"))
        minValueLanding = removePadValues(filterData?.atime_from.toString())
        maxValueLanding = removePadValues(filterData?.atime_to.toString())
        range_seekbar_ob_landing.setMinValue(0f).setMaxValue(23.59f)
            .setMinStartValue(minValueLanding.toFloat())
            .setMaxStartValue(zeroCheck(maxValueLanding).toFloat())
            .apply()

        textMin_in_takeoff.text = removePadValues(nullDefaultValues(filterData?.ret_dtime_from, "0"))
        textMax_in_takeoff.text = removePadValues(nullDefaultValues(filterData?.ret_dtime_to, "23:59"))
        minValueInTakeOff = removePadValues(filterData?.ret_dtime_from.toString())
        maxValueInTakeOff = removePadValues(filterData?.ret_dtime_to.toString())
        range_seekbar_in_takeoff.setMinValue(0f).setMaxValue(23.59f)
            .setMinStartValue(minValueInTakeOff.toFloat())
            .setMaxStartValue(zeroCheck(maxValueInTakeOff).toFloat())
            .apply()
        textMinOblanding.text = removePadValues(nullDefaultValues(filterData?.ret_atime_from, "0"))
        textMaxOblanding.text = removePadValues(nullDefaultValues(filterData?.ret_atime_to, "23:59"))
        minValueInLanding = removePadValues(filterData?.ret_atime_from.toString())
        maxValueInLanding = removePadValues(filterData?.ret_atime_to.toString())
        range_seekbar_in_landing.setMinValue(0f).setMaxValue(23.59f)
            .setMinStartValue(minValueInLanding.toFloat())
            .setMaxStartValue(zeroCheck(maxValueInLanding).toFloat())
            .apply()
        switchButton_direct.isChecked = filterData?.max_stopovers?.contains(0)!!
        switchButton_1stop.isChecked = filterData?.max_stopovers?.contains(1)!!
        switchButton_2stops.isChecked = filterData?.max_stopovers?.contains(2)!!
    }

    private fun zeroCheck(maxDuration: String): String {
        if (maxDuration == "0:00")
            return "23.59"
        else if (maxDuration == "0")
            return "23.59"
        else if (maxDuration == "00:00")
            return "23.59"
        else if (maxDuration == "00")
            return "23.59"
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
                    minValueTakeOff + ":00".padEnd(2),
                    maxValueTakeOff + ":00".padEnd(2),
                    minValueLanding + ":00".padEnd(2),
                    maxValueLanding + ":00".padEnd(2),
                    "0:00",
                    "0:00",
                    "0:00",
                    "0:00",
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
                    minValueTakeOff + ":00".padEnd(2),
                    maxValueTakeOff + ":00".padEnd(2),
                    minValueLanding + ":00".padEnd(2),
                    maxValueLanding + ":00".padEnd(2),
                    minValueInTakeOff + ":00".padEnd(2),
                    maxValueInTakeOff + ":00".padEnd(2),
                    minValueInLanding + ":00".padEnd(2),
                    maxValueInLanding + ":00".padEnd(2),
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

    private fun setRangeSeekbarObTakeOff() {
        range_seekbar_outbound_trip.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textMin_outbound_takeofff.text = minValue.toString()
            textMax_outbound_takeofff.text = maxValue.toString()
            if (maxValue == 24)
                textMax_outbound_takeofff.text = "23.59"
        }
        range_seekbar_outbound_trip.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            /*Log.d(
                "CRS=>",
                minValue.toString() + " : " + maxValue.toString()
            )*/
            minValueTakeOff = minValue.toString()
            maxValueTakeOff = maxValue.toString()
        }
    }

    private fun setRangeSeekbarForObLanding() {
        range_seekbar_ob_landing.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textMinOblanding.text = minValue.toString()
            textMaxOblanding.text = maxValue.toString()
            if (maxValue == 24)
                textMax_outbound_takeofff.text = "23.59"
        }
        range_seekbar_ob_landing.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            minValueLanding = minValue.toString()
            maxValueLanding = maxValue.toString()
        }
    }

    private fun setRangeSeekbarIbTakeOff() {
        range_seekbar_in_takeoff.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textMin_in_takeoff.text = minValue.toString()
            textMax_in_takeoff.text = maxValue.toString()
            if (maxValue == 24)
                textMax_in_takeoff.text = "23.59"
        }
        range_seekbar_in_takeoff.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            /*Log.d(
                "CRS=>",
                minValue.toString() + " : " + maxValue.toString()
            )*/
            minValueInTakeOff = minValue.toString()
            maxValueInTakeOff = maxValue.toString()
        }
    }

    private fun setRangeSeekbarForIbLanding() {
        range_seekbar_in_landing.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textMinInlanding.text = minValue.toString()
            textMaxInlanding.text = maxValue.toString()
            if (maxValue == 24)
                textMax_outbound_takeofff.text = "23.59"
        }
        range_seekbar_in_landing.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            minValueInLanding = minValue.toString()
            maxValueInLanding = maxValue.toString()
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


    /*return fragmentManager?.popBackStackImmediate()!!
    else

        activity?.finish()
    return true*/

}
