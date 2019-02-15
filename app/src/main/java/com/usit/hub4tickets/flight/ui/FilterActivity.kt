package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FilterModel
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Pref
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_filter.*
import java.text.DecimalFormat


class FilterActivity : AppCompatActivity() {
    private var minValuePrice: String = "0"
    private var maxValuePrice: String = "1000000"
    private var minValue: String = "0"
    private var maxValue: String = "0"
    private var minValuedef: String = "0"
    private var maxValuedef: String = "0"
    private var minValueDuration: String = "0"
    private var maxValueDuration: String = "0"
    private var minValueLanding: String = "00:00"
    private var maxValueLanding: String = "00:00"
    private var minValueTakeOff: String = "00:00"
    private var maxValueTakeOff: String = "00:00"
    private var minValueInLanding: String = "00:00"
    private var maxValueInLanding: String = "00:00"
    private var minValueInTakeOff: String = "00:00"
    private var maxValueInTakeOff: String = "00:00"
    var filterData: FilterModel.Filter? = null
    private val SMALLEST_HOUR_FRACTION: Float = 60f
    private val COMMON_FRACTION: Int = 60
    private val deciFormat = DecimalFormat("00")

    private var activityTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_filter)
        setToolBar()
        setOtherListeners()
        setRangeSeekbarForPrice()
        if (savedInstanceState != null) {
            filterData = savedInstanceState.getParcelable(Constant.Path.FILTER_DATA)
            activityTitle = savedInstanceState.getString(Constant.Path.ACTIVITY_TITLE)
            if (null != filterData)
                init()
            else
                setDefaultValues()
        }
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
        }
        if (activityTitle.equals("FragmentMulticity")) {
            ll_stops.visibility = View.GONE
            ll_time.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putString(Constant.Path.ACTIVITY_TITLE, "FragmentReturn")
            putParcelable(Constant.Path.FILTER_DATA, filterData)
        })
    }

    private fun setDefaultValues() {
        //price
        textMin_price.text = minValue
        textMax_price.text = maxValue
        Pref.setValue(this, Constant.Path.DEF_MAX_PRICE, maxValue)
        Pref.setValue(this, Constant.Path.DEF_MIN_PRICE, minValue)
        range_seekbar_price.setMinValue(minValue.toFloat()).setMaxValue(maxValue.toFloat())
            .setMinStartValue(minValue.toFloat())
            .setMaxStartValue(maxValue.toFloat()).apply()

        //duration
        rb_duration.setMinValue(0f).setMaxValue(100f).setMinStartValue(0f).apply()
        setRangeSeekbarForDuration()

        //outbound take off
        rb_ob_toff.setConnectingLineColor(R.color.colorPrimary)
        rb_ob_toff.setTickCount(COMMON_FRACTION * 24)
        rb_ob_toff.setTickHeight(0f)
        rb_ob_toff.setBarWeight(10f)
        rb_ob_toff.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_outbound_takeofff, textMax_outbound_takeofff, leftThumbIndex, rightThumbIndex)
            minValueTakeOff = textMin_outbound_takeofff.text.toString()
            maxValueTakeOff = textMax_outbound_takeofff.text.toString()
        }

        //outbound landing
        rb_ob_landing.setConnectingLineColor(R.color.colorPrimary)
        rb_ob_landing.setTickCount(COMMON_FRACTION * 24)
        rb_ob_landing.setTickHeight(0f)
        rb_ob_landing.setBarWeight(10f)
        rb_ob_landing.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_ob_landing, textMax_ob_landing, leftThumbIndex, rightThumbIndex)
            minValueLanding = textMin_ob_landing.text.toString()
            maxValueLanding = textMax_ob_landing.text.toString()
        }

        //inbound take off
        rb_in_toff.setConnectingLineColor(R.color.colorPrimary)
        rb_in_toff.setTickCount(COMMON_FRACTION * 24)
        rb_in_toff.setTickHeight(0f)
        rb_in_toff.setBarWeight(10f)
        rb_in_toff.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_inbound_takeofff, textMax_inbound_takeofff, leftThumbIndex, rightThumbIndex)
            minValueInTakeOff = textMin_inbound_takeofff.text.toString()
            maxValueInTakeOff = textMax_inbound_takeofff.text.toString()
        }

        //inbound landing
        rb_in_landing.setConnectingLineColor(R.color.colorPrimary)
        rb_in_landing.setTickCount(COMMON_FRACTION * 24)
        rb_in_landing.setTickHeight(0f)
        rb_in_landing.setBarWeight(10f)
        rb_in_landing.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_in_landing, textMax_in_landing, leftThumbIndex, rightThumbIndex)
            minValueInLanding = textMin_in_landing.text.toString()
            maxValueInLanding = textMax_in_landing.text.toString()
        }
    }

    private fun getSelectedValue(
        minTextView: TextView?,
        maxTextView: TextView?,
        leftThumbIndex: Int,
        rightThumbIndex: Int
    ) {
        minTextView?.text = "" + leftThumbIndex
        maxTextView?.text = "" + rightThumbIndex
        var minHour = Math.round(leftThumbIndex / SMALLEST_HOUR_FRACTION)
        var minMinute =
            Math.round((COMMON_FRACTION / SMALLEST_HOUR_FRACTION) * (leftThumbIndex % SMALLEST_HOUR_FRACTION))
        var maxHour = Math.round(rightThumbIndex / SMALLEST_HOUR_FRACTION)
        var maxMinute =
            Math.round((COMMON_FRACTION / SMALLEST_HOUR_FRACTION) * (rightThumbIndex % SMALLEST_HOUR_FRACTION))

        if (minHour <= 0 && minMinute <= 0) {
            minHour = 0
            minMinute = 0
        }
        if (minHour <= 0 && minMinute <= 30) {
            minHour = 0
            minMinute = 0
        }
        if (maxHour >= 24)
            maxHour = 23

        if (maxHour >= 24 && maxMinute >= 0)
            maxMinute = 59

        if (maxHour >= 23 && maxMinute > 30)
            maxMinute = 59

        minTextView?.text = deciFormat.format(minHour) + ":" + deciFormat.format(minMinute)
        maxTextView?.text = deciFormat.format(maxHour) + ":" + deciFormat.format(maxMinute)

    }

    private fun getLeftAndRightIndex(leftThumbIndex: String): Int {
        val leftIndexHours = leftThumbIndex.split(":")[0]
        val leftIndexMin = leftThumbIndex.split(":")[1]
        val hoursToMinute = Math.round(leftIndexHours.toInt() * SMALLEST_HOUR_FRACTION)
        return hoursToMinute.plus(leftIndexMin.toInt())
    }

    private fun init() {
        //duration after apply
        textMin_duration.text = maxValueDuration
        maxValueDuration = filterData?.max_fly_duration.toString()
        rb_duration.setMinStartValue(maxValueDuration.toFloat()).setMaxValue(100f).apply()
        setRangeSeekbarForDuration()

        //maxValueDuration.toFloat()
        //price after apply
        textMin_price.text = filterData?.price_from
        textMax_price.text = filterData?.price_to
        minValuedef = Pref.getValue(this, Constant.Path.DEF_MIN_PRICE, "0")!!
        maxValuedef = Pref.getValue(this, Constant.Path.DEF_MAX_PRICE, "0")!!
        minValuePrice = filterData?.price_from.toString()
        maxValuePrice = filterData?.price_to.toString()
        range_seekbar_price.setMinValue(minValuedef.toFloat()).setMaxValue(maxValuedef.toFloat())
            .setMinStartValue(minValuePrice.toFloat())
            .setMaxStartValue(maxValuePrice.toFloat()).apply()

        if (activityTitle.equals("FragmentOneWay")) {
            ll_inbound.visibility = View.GONE

        } else if (activityTitle.equals("FragmentMulticity")) {
            ll_stops.visibility = View.GONE
            ll_time.visibility = View.GONE
        } else
            ll_inbound.visibility = View.VISIBLE

        //outbound_takeoff after apply
        textMin_outbound_takeofff.text = nullDefaultValues(filterData?.dtime_from, "00:00")
        textMax_outbound_takeofff.text = nullDefaultValues(filterData?.dtime_to, "23:59")
        minValueTakeOff = filterData?.dtime_from.toString()
        maxValueTakeOff = filterData?.dtime_to.toString()

        rb_ob_toff.setConnectingLineColor(R.color.colorPrimary)
        rb_ob_toff.setTickCount(COMMON_FRACTION * 24)
        rb_ob_toff.setTickHeight(0f)
        rb_ob_toff.setBarWeight(10f)

        try {
            rb_ob_toff.setThumbIndices(getLeftAndRightIndex(minValueTakeOff), getLeftAndRightIndex(maxValueTakeOff))
        } catch (e: Exception) {
            e.message
        }
        rb_ob_toff.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_outbound_takeofff, textMax_outbound_takeofff, leftThumbIndex, rightThumbIndex)
            minValueTakeOff = textMin_outbound_takeofff.text.toString()
            maxValueTakeOff = textMax_outbound_takeofff.text.toString()
        }
        //ob_landing after apply
        textMin_ob_landing.text = nullDefaultValues(filterData?.atime_from, "0:00")
        textMax_ob_landing.text = nullDefaultValues(filterData?.atime_to, "23:59")
        minValueLanding = filterData?.atime_from.toString()
        maxValueLanding = filterData?.atime_to.toString()

        rb_ob_landing.setConnectingLineColor(R.color.colorPrimary)
        rb_ob_landing.setTickCount(COMMON_FRACTION * 24)
        rb_ob_landing.setTickHeight(0f)
        rb_ob_landing.setBarWeight(10f)
        try {
            rb_ob_landing.setThumbIndices(getLeftAndRightIndex(minValueLanding), getLeftAndRightIndex(maxValueLanding))
        } catch (e: Exception) {
        }
        rb_ob_landing.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_ob_landing, textMax_ob_landing, leftThumbIndex, rightThumbIndex)
            minValueLanding = textMin_ob_landing.text.toString()
            maxValueLanding = textMax_ob_landing.text.toString()

        }
        //inbound_takeoff after apply
        textMin_inbound_takeofff.text = nullDefaultValues(filterData?.ret_dtime_from, "0:00")
        textMax_inbound_takeofff.text = nullDefaultValues(filterData?.ret_dtime_to, "23:59")
        minValueInTakeOff = filterData?.ret_dtime_from.toString()
        maxValueInTakeOff = filterData?.ret_dtime_to.toString()

        rb_in_toff.setConnectingLineColor(R.color.colorPrimary)
        rb_in_toff.setTickCount(COMMON_FRACTION * 24)
        rb_in_toff.setTickHeight(0f)
        rb_in_toff.setBarWeight(10f)
        try {
            rb_in_toff.setThumbIndices(getLeftAndRightIndex(minValueInTakeOff), getLeftAndRightIndex(maxValueInTakeOff))
        } catch (e: Exception) {
        }
        rb_in_toff.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_inbound_takeofff, textMax_inbound_takeofff, leftThumbIndex, rightThumbIndex)
            minValueInTakeOff = textMin_inbound_takeofff.text.toString()
            maxValueInTakeOff = textMax_inbound_takeofff.text.toString()

        }
        //in_landing after apply
        textMin_in_landing.text = nullDefaultValues(filterData?.ret_atime_from, "0:00")
        textMax_in_landing.text = nullDefaultValues(filterData?.ret_atime_to, "23:59")
        minValueInLanding = filterData?.ret_atime_from.toString()
        maxValueInLanding = filterData?.ret_atime_to.toString()

        rb_in_landing.setConnectingLineColor(R.color.colorPrimary)
        rb_in_landing.setTickCount(COMMON_FRACTION * 24)
        rb_in_landing.setTickHeight(0f)
        rb_in_landing.setBarWeight(10f)
        try {
            rb_in_landing.setThumbIndices(
                getLeftAndRightIndex(minValueInLanding),
                getLeftAndRightIndex(maxValueInLanding)
            )
        } catch (e: Exception) {
        }
        rb_in_landing.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_in_landing, textMax_in_landing, leftThumbIndex, rightThumbIndex)
            minValueInLanding = textMin_in_landing.text.toString()
            maxValueInLanding = textMax_in_landing.text.toString()

        }

        switchButton_direct.isChecked = filterData?.max_stopovers?.contains(0)!!
        switchButton_1stop.isChecked = filterData?.max_stopovers?.contains(1)!!
        switchButton_2stops.isChecked = filterData?.max_stopovers?.contains(2)!!
    }


    private fun nullDefaultValues(bundleData: String?, defaultValue: String): String {
        if (bundleData.equals("00:00"))
            return defaultValue
        else
            return bundleData.toString()
    }

    private fun setOtherListeners() {

        button_dialog_cancel.setOnClickListener { onBackPressed() }

        var maxStopovers: ArrayList<Int> = ArrayList()
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
                    maxValueDuration,
                    minValueTakeOff,
                    maxValueTakeOff,
                    minValueLanding,
                    maxValueLanding,
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
                    maxValueDuration,
                    minValueTakeOff,
                    maxValueTakeOff,
                    minValueLanding,
                    maxValueLanding,
                    minValueInTakeOff,
                    maxValueInTakeOff,
                    minValueInLanding,
                    maxValueInLanding,
                    maxStopovers
                )
                val intent = Intent()
                intent.putExtra(Constant.Path.FILTER_DATA, filterModel)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else if (activityTitle.equals("FragmentMulticity")) {
                val filterModel: FilterModel.Filter = FilterModel.Filter(
                    minValuePrice,
                    maxValuePrice,
                    maxValueDuration,
                    "0:00",
                    "0:00",
                    "0:00",
                    "0:00",
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
            }

        }

    }

    private fun setToolBar() {
        titleToolBar?.text = resources.getString(com.usit.hub4tickets.R.string.filter)
        mainToolbar?.setNavigationOnClickListener { finish() }
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

    private fun setRangeSeekbarForDuration() {
        rb_duration.setOnSeekbarChangeListener { maxValue ->
            textMin_duration.text = maxValue.toString()
            if (maxValue.toFloat().toInt() == 0)
                textMax_duration.text = "Any"
            else if (maxValue.toFloat().toInt() > 48)
                textMax_duration.text = "Any"
            else
                textMax_duration.text = "48"

        }
        rb_duration.setOnSeekbarFinalValueListener { maxValue ->
            maxValueDuration = maxValue.toString()

        }

    }
}
