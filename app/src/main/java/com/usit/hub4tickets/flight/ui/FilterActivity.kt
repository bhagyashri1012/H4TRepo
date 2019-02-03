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
import android.widget.Toast
import java.text.DecimalFormat
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.SeekBar
import android.widget.TextView
import com.edmodo.rangebar.RangeBar
import kotlinx.android.synthetic.main.list_item.view.*


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

    private var activityTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_filter)
    }

    override fun onResume() {
        super.onResume()
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
        }
    }
    private val SMALLEST_HOUR_FRACTION: Int = 60
    private val COMMON_FRACTION: Int = 60
    val deciFormat = DecimalFormat("00")

    private fun setDefaultValues() {
        setRangeSeekbarForPrice()
        //price
        textMin_price.text = minValue
        textMax_price.text = maxValue
        Pref.setValue(this, Constant.Path.DEF_MAX_PRICE, maxValue)
        Pref.setValue(this, Constant.Path.DEF_MIN_PRICE, minValue)
        range_seekbar_price.setMinValue(minValue.toFloat()).setMaxValue(maxValue.toFloat())
            .setMinStartValue(minValue.toFloat())
            .setMaxStartValue(maxValue.toFloat()).apply()

      //duration
        rb_duration.progressDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
        rb_duration.thumb.setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN)
        rb_duration.thumb.setBounds(10,10,10,10)
        rb_duration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(thumbRadiusSeek: SeekBar, progress: Int, fromUser: Boolean) {
                textMax_duration.text=progress.toString()
                maxValueDuration=progress.toString()
                if(progress>48)
                    textMax_duration.text="Any"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        /*rb_duration.setConnectingLineColor(R.color.colorPrimary)
        rb_duration.setTickHeight(0f)
        rb_duration.setThumbIndices(0,48)
        rb_duration.setThumbColorNormal(R.color.colorPrimary)
        rb_duration.setThumbColorPressed(R.color.colorPrimaryDark)
        rb_duration.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            textMin_duration?.text = "" + leftThumbIndex
            textMax_duration?.text = "" + rightThumbIndex
            minValueDuration=textMin_duration.text.toString()
            maxValueDuration=textMax_duration.text.toString()
        }*/


        //outbound take off
        rb_ob_toff.setConnectingLineColor(R.color.colorPrimary)
        rb_ob_toff.setTickCount(COMMON_FRACTION * 24)
        rb_ob_toff.setTickHeight(0f)
        rb_ob_toff.setThumbRadius(10f)
        rb_ob_toff.setBarWeight(3f)
        rb_ob_toff.setConnectingLineWeight(3f)
        rb_ob_toff.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_outbound_takeofff,textMax_outbound_takeofff,leftThumbIndex,rightThumbIndex)
            minValueTakeOff=textMin_outbound_takeofff.text.toString()
            maxValueTakeOff=textMax_outbound_takeofff.text.toString()

        }

        //outbound landing
        rb_ob_landing.setConnectingLineColor(R.color.colorPrimary)
        rb_ob_landing.setTickCount(COMMON_FRACTION * 24)
        rb_ob_landing.setTickHeight(0f)
        rb_ob_landing.setThumbRadius(10f)
        rb_ob_landing.setBarWeight(3f)
        rb_ob_landing.setConnectingLineWeight(3f)
        rb_ob_landing.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_ob_landing,textMax_ob_landing,leftThumbIndex,rightThumbIndex)
            minValueLanding=textMin_ob_landing.text.toString()
            maxValueLanding=textMax_ob_landing.text.toString()

        }

        //inbound take off
        rb_in_toff.setConnectingLineColor(R.color.colorPrimary)
        rb_in_toff.setTickCount(COMMON_FRACTION * 24)
        rb_in_toff.setTickHeight(0f)
        rb_in_toff.setThumbRadius(10f)
        rb_in_toff.setBarWeight(3f)
        rb_in_toff.setConnectingLineWeight(3f)
        rb_in_toff.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_inbound_takeofff,textMax_inbound_takeofff,leftThumbIndex,rightThumbIndex)
            minValueInTakeOff=textMin_inbound_takeofff.text.toString()
            maxValueInTakeOff=textMax_inbound_takeofff.text.toString()

        }

        //inbound landing
        rb_in_landing.setConnectingLineColor(R.color.colorPrimary)
        rb_in_landing.setTickCount(COMMON_FRACTION * 24)
        rb_in_landing.setTickHeight(0f)
        rb_in_landing.setThumbRadius(10f)
        rb_in_landing.setBarWeight(3f)
        rb_in_landing.setConnectingLineWeight(3f)
        rb_in_landing.setOnRangeBarChangeListener { rangeBar, leftThumbIndex, rightThumbIndex ->
            getSelectedValue(textMin_in_landing,textMax_in_landing,leftThumbIndex,rightThumbIndex)
            minValueLanding=textMin_in_landing.text.toString()
            maxValueLanding=textMax_in_landing.text.toString()

        }


    }

    private fun getSelectedValue(minTextView: TextView?, maxTextView: TextView?, leftThumbIndex: Int, rightThumbIndex: Int) {
        minTextView?.text = "" + leftThumbIndex
        maxTextView?.text = "" + rightThumbIndex
        val minHour = leftThumbIndex / SMALLEST_HOUR_FRACTION
        val minMinute = (COMMON_FRACTION/SMALLEST_HOUR_FRACTION.toFloat())* (leftThumbIndex % SMALLEST_HOUR_FRACTION)
        val maxHour = rightThumbIndex / SMALLEST_HOUR_FRACTION
        val maxMinute = (COMMON_FRACTION/SMALLEST_HOUR_FRACTION.toFloat()) * (rightThumbIndex % SMALLEST_HOUR_FRACTION)
        minTextView?.text = deciFormat.format(minHour) + ":" + deciFormat.format(minMinute)
        maxTextView?.text = deciFormat.format(maxHour) + ":" + deciFormat.format(maxMinute)
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

        textMin_outbound_takeofff.text = nullDefaultValues(filterData?.dtime_from, "00:00")
        textMax_outbound_takeofff.text = nullDefaultValues(filterData?.dtime_to, "23:59")
        minValueTakeOff = filterData?.dtime_from.toString()
        maxValueTakeOff = filterData?.dtime_to.toString()


        textMin_ob_landing.text = nullDefaultValues(filterData?.atime_from, "0:00")
        textMax_ob_landing.text = nullDefaultValues(filterData?.atime_to, "23:59")
        minValueLanding = filterData?.atime_from.toString()
        maxValueLanding = filterData?.atime_to.toString()


        textMin_inbound_takeofff.text = nullDefaultValues(filterData?.ret_dtime_from, "0")
        textMax_inbound_takeofff.text = nullDefaultValues(filterData?.ret_dtime_to, "23:59")
        minValueInTakeOff = filterData?.ret_dtime_from.toString()
        maxValueInTakeOff = filterData?.ret_dtime_to.toString()


        textMin_in_landing.text = nullDefaultValues(filterData?.ret_dtime_from, "0")
        textMax_in_landing.text = nullDefaultValues(filterData?.ret_dtime_to, "23:59")
        minValueInLanding = filterData?.ret_atime_from.toString()
        maxValueInLanding = filterData?.ret_atime_to.toString()



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
                    maxValueDuration,
                    minValueTakeOff,
                    maxValueTakeOff,
                    minValueLanding ,
                    maxValueLanding ,
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
                    minValueLanding ,
                    maxValueLanding ,
                    minValueInTakeOff ,
                    maxValueInTakeOff ,
                    minValueInLanding ,
                    maxValueInLanding ,
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

}
