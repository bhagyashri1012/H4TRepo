package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.squareup.timessquare.CalendarCellDecorator
import com.squareup.timessquare.CalendarPickerView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.utils.Constant
import kotlinx.android.synthetic.main.activity_calender.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.text.SimpleDateFormat
import java.util.*


class CalenderActivity : BaseActivity() {
    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    private var departure_date: String = ""
    private var defDepartureDate: String = ""
    private var returnDate: String = ""
    private var defReturnDate: String = ""
    private var action: String = ""
    private var activityTitle: String = ""
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private val dateFormatInput = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        initToolBar()
        init()
        OnDoneClick()
    }

    private fun OnDoneClick() {

        get_selected_dates.setOnClickListener {
            departure_date = dateFormat.format(
                dateFormatInput.parse(calendar_view.selectedDates[0].toString())
            )
            returnDate = dateFormat.format(
                dateFormatInput.parse(calendar_view.selectedDates[calendar_view.selectedDates.lastIndex].toString())
            )

            Log.d("departure date", "$departure_date   return date$returnDate")

            if (returnDate.isNotBlank()) {
                val intent = Intent()
                intent.putExtra(Constant.Path.RETURN_SELECTED_DEP_DATE, departure_date)
                intent.putExtra(Constant.Path.RETURN_SELECTED_RET_DATE, returnDate)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun init() {
        if (null != intent.extras) {
            defDepartureDate = intent.getStringExtra(Constant.Path.SELECTED_FROM_DATE)
            defReturnDate = intent.getStringExtra(Constant.Path.SELECTED_TO_DATE)
            action = intent.getStringExtra(Constant.Path.ACTION_TITLE)
            activityTitle = intent.getStringExtra(Constant.Path.ACTIVITY_TITLE)
        }
        val dates = ArrayList<Date>()
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        if (activityTitle == "FragmentReturn") {
            val today = Calendar.getInstance()
            if (!defDepartureDate.isNullOrBlank())
                today.time = dateFormat.parse(defDepartureDate)
            else
                today.add(Calendar.DATE, 1)
            dates.add(today.time)
            if (!defReturnDate.isNullOrBlank())
                today.time = dateFormat.parse(defReturnDate)
            else
                today.add(Calendar.DATE, 5)
            dates.add(today.time)
            calendar_view.decorators = emptyList<CalendarCellDecorator>()
            calendar_view.init(Date(), nextYear.time)
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(dates)
            if (calendar_view.selectedDates.size > 1) {
                get_selected_dates.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorPrimary))
                get_selected_dates.isEnabled = true
            } else {
                get_selected_dates.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.white_grey))
                get_selected_dates.isEnabled = false
            }

            calendar_view.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                override fun onDateSelected(date: Date) {

                    if (calendar_view.selectedDates.size > 1) {
                        get_selected_dates.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorPrimary))
                        get_selected_dates.isEnabled = true
                    } else {

                        if (action == "ReturnClick") {
                            val dates = ArrayList<Date>()
                            val nextYear = Calendar.getInstance()
                            nextYear.add(Calendar.YEAR, 1)
                            val today = Calendar.getInstance()
                            if (!defDepartureDate.isNullOrBlank())
                                today.time = dateFormat.parse(defDepartureDate)
                            else
                                today.add(Calendar.DATE, 1)
                            dates.add(today.time)
                            today.time = date
                            dates.add(today.time)
                            calendar_view.decorators = emptyList<CalendarCellDecorator>()
                            calendar_view.init(Date(), nextYear.time) //
                                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                                .withSelectedDates(dates)
                        } else {
                            get_selected_dates.setBackgroundColor(
                                ContextCompat.getColor(
                                    baseContext,
                                    R.color.white_grey
                                )
                            )
                            get_selected_dates.isEnabled = false
                        }
                    }
                }

                override fun onDateUnselected(date: Date) {
                    if (calendar_view.selectedDates.size <= 1) {
                        //Toast.makeText(applicationContext, "UnSelected Date is : $date", Toast.LENGTH_SHORT).show()
                        get_selected_dates.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.white_grey))
                        get_selected_dates.isEnabled = false
                    } else {
                        //Toast.makeText(applicationContext, "UnSelected else is : $date", Toast.LENGTH_SHORT).show()
                        get_selected_dates.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorPrimary))
                        get_selected_dates.isEnabled = true
                    }

                }
            })

        } else if (activityTitle == "FragmentOneWay") {
            get_selected_dates.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorPrimary))
            get_selected_dates.isEnabled = true

            val today = Calendar.getInstance()
            if (!defDepartureDate.isNullOrBlank())
                today.time = dateFormat.parse(defDepartureDate)
            else
                today.add(Calendar.DATE, 1)
            dates.add(today.time)
            calendar_view.decorators = emptyList<CalendarCellDecorator>()
            calendar_view.init(Date(), nextYear.time)
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDates(dates)
        }
    }

    private fun initToolBar() {
        title = resources.getString(R.string.select_dates)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
