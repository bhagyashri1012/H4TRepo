package com.usit.hub4tickets.flight.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.squareup.timessquare.CalendarCellDecorator
import com.squareup.timessquare.CalendarPickerView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Utility
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
    private var defDepartureDateAfter: String = ""
    private var defDepartureDateBfr: String = ""
    private var position: Int = 0
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
            if (activityTitle == "FragmentMultiCity") {
                if (isBefore) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.calender_validations),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (isAfter) {
                    Toast.makeText(
                        applicationContext,
                        "Departure date cannot be greater than the next departure date",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    departure_date = dateFormat.format(
                        dateFormatInput.parse(calendar_view.selectedDates[0].toString())
                    )
                    val intent = Intent()
                    intent.putExtra(Constant.Path.RETURN_SELECTED_DEP_DATE, departure_date)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            } else {
                if (activityTitle == "FragmentReturn" && action == "DepartureClick") {
                    if (calendar_view.selectedDates.size <= 1)
                        Toast.makeText(applicationContext, "Select Return Date", Toast.LENGTH_SHORT).show()
                    else {
                        departure_date = dateFormat.format(
                            dateFormatInput.parse(calendar_view.selectedDates[0].toString())
                        )
                        returnDate = dateFormat.format(
                            dateFormatInput.parse(calendar_view.selectedDates[calendar_view.selectedDates.lastIndex].toString())
                        )
                        //Log.d("departure date", "$departure_date   return date$returnDate")

                        if (returnDate.isNotBlank()) {
                            val intent = Intent()
                            intent.putExtra(Constant.Path.RETURN_SELECTED_DEP_DATE, departure_date)
                            intent.putExtra(Constant.Path.RETURN_SELECTED_RET_DATE, returnDate)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                } else {
                    if (isBefore) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.calender_validations),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        departure_date = dateFormat.format(
                            dateFormatInput.parse(calendar_view.selectedDates[0].toString())
                        )
                        returnDate = dateFormat.format(
                            dateFormatInput.parse(calendar_view.selectedDates[calendar_view.selectedDates.lastIndex].toString())
                        )
                        //Log.d("departure date", "$departure_date   return date$returnDate")
                        if (returnDate.isNotBlank()) {
                            val intent = Intent()
                            intent.putExtra(Constant.Path.RETURN_SELECTED_DEP_DATE, departure_date)
                            intent.putExtra(Constant.Path.RETURN_SELECTED_RET_DATE, returnDate)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private var isBefore: Boolean = false
    private var isAfter: Boolean = false

    private fun init() {
        if (null != intent.extras) {
            defDepartureDate = intent.getStringExtra(Constant.Path.SELECTED_FROM_DATE)
            action = intent.getStringExtra(Constant.Path.ACTION_TITLE)
            activityTitle = intent.getStringExtra(Constant.Path.ACTIVITY_TITLE)
            if (activityTitle == "FragmentReturn")
                defReturnDate = intent.getStringExtra(Constant.Path.SELECTED_TO_DATE)
            if (activityTitle == "FragmentMultiCity") {
                defDepartureDateAfter = intent.getStringExtra(Constant.Path.SELECTED_FROM_DATE_AFTER)
                defDepartureDateBfr = intent.getStringExtra(Constant.Path.SELECTED_FROM_DATE_BEFORE)
                position = intent.getIntExtra(Constant.Path.POSITION, 0)
            }

        }
        val dates = ArrayList<Date>()

        if (activityTitle == "FragmentReturn") {
            val nextYear = Calendar.getInstance()
            //if (action == "DepartureClick") {
            nextYear.add(Calendar.YEAR, 1)
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

        }  else if (activityTitle == "FragmentMultiCity") {
            val nextYear = Calendar.getInstance()
            nextYear.add(Calendar.YEAR, 1)
            val today = Calendar.getInstance()
            if (!defDepartureDateBfr.isNullOrBlank())
                today.time = dateFormat.parse(defDepartureDateBfr)
            else
                today.add(Calendar.DATE, 1)
            dates.add(today.time)
            calendar_view.decorators = emptyList<CalendarCellDecorator>()
            calendar_view.init(Date(), nextYear.time)
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDates(dates)
        }else {
            val nextYear = Calendar.getInstance()
            nextYear.add(Calendar.YEAR, 1)
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

        calendar_view.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (activityTitle == "FragmentReturn") {
                    if (action == "ReturnClick") {
                        if (!defDepartureDate.isNullOrBlank()) {
                            if (dateFormat.parse(defDepartureDate).after(date)) {
                                isBefore = true
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.calender_validations),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                isBefore = false
                                val dates = ArrayList<Date>()
                                val nextYear = Calendar.getInstance()
                                nextYear.add(Calendar.YEAR, 1)
                                val today = Calendar.getInstance()
                                today.time = dateFormat.parse(defDepartureDate)
                                dates.add(today.time)
                                today.time = date
                                dates.add(today.time)
                                calendar_view.decorators = emptyList<CalendarCellDecorator>()
                                calendar_view.init(Date(), nextYear.time)
                                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                                    .withSelectedDates(dates)
                                calendar_view.smoothScrollByOffset(0)
                                calendar_view.scrollToDate(date)
                            }
                        } else {
                            val dates = ArrayList<Date>()
                            val nextYear = Calendar.getInstance()
                            nextYear.add(Calendar.YEAR, 1)
                            val today = Calendar.getInstance()
                            today.add(Calendar.DATE, 1)
                            dates.add(today.time)
                            today.time = date
                            dates.add(today.time)
                            calendar_view.decorators = emptyList<CalendarCellDecorator>()
                            calendar_view.init(Date(), nextYear.time)
                                .inMode(CalendarPickerView.SelectionMode.RANGE)
                                .withSelectedDates(dates)
                            calendar_view.smoothScrollByOffset(0)
                            calendar_view.scrollToDate(date)
                        }
                    }
                } else if (activityTitle == "FragmentMultiCity") {
                    if (position.toInt() == 0)
                        defDepartureDate = Utility.getCurrentDateNow()
                    if (defDepartureDate.isNotBlank()) {
                        if (dateFormat.parse(defDepartureDate).after(date)) {
                            isBefore = true
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.calender_validations),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            isBefore = false
                            if (defDepartureDateAfter.isNotBlank()) {
                                if (dateFormat.parse(defDepartureDateAfter).before(date)) {
                                    isAfter = true
                                    Toast.makeText(
                                        applicationContext,
                                        "Departure date cannot be greater than the next departure date",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else
                                    isAfter = false

                            }
                        }
                    }
                }
            }

            override fun onDateUnselected(date: Date) {
                //Toast.makeText(applicationContext, "UnSelected Date is : $date", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun initToolBar() {
        title = resources.getString(R.string.select_dates)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
