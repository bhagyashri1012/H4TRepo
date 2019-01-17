package com.usit.hub4tickets.account.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.model.DashboardViewModel
import com.usit.hub4tickets.domain.presentation.presenters.DashboardPresenter
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter
import com.usit.hub4tickets.domain.presentation.presenters.ProfilePresenter.MainView.ViewState.*
import com.usit.hub4tickets.domain.presentation.screens.BaseActivity
import com.usit.hub4tickets.domain.presentation.screens.main.DashboardPresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.ProfilePresenterImpl
import com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModel
import com.usit.hub4tickets.search.CommonSearchActivity
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import com.usit.hub4tickets.utils.view.dialog.CustomDialogPresenter
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.common_toolbar.*

class PersonalInfoActivity : BaseActivity(), ProfilePresenter.MainView, DashboardPresenter.MainView {
    private val TIMEZONE_SELECTION_REQUEST = 203
    private val AIRPORT_SELECTION_REQUEST = 301
    private val LOCATION_SELECTION_REQUEST = 201
    private val LANGUAGE_SELECTION_REQUEST = 102
    private val STATE_SELECTION_REQUEST = 103
    private val CITY_SELECTION_REQUEST = 104
    private lateinit var model: ProfileViewModel
    private lateinit var modelDashboard: DashboardViewModel
    private lateinit var presenter: ProfilePresenter
    private lateinit var presenterDashboard: DashboardPresenter
    override fun doRetrieveProfileModel(): ProfileViewModel = this.model
    override fun doRetrieveModel(): DashboardViewModel = this.modelDashboard

    override fun showState(viewState: DashboardPresenter.MainView.ViewState) {

        when (viewState) {
            DashboardPresenter.MainView.ViewState.IDLE -> showProgress(false)
            DashboardPresenter.MainView.ViewState.LOADING -> showProgress(true)
            DashboardPresenter.MainView.ViewState.TIME_ZONE_SUCCESS -> {
                showProgress(false)
                openSearchActivityTimeZones(
                    modelDashboard.dashboradTimeZoneDomain.responseData as ArrayList<DashboardViewModel.TimeZoneResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    TIMEZONE_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.AIRPORTS_SUCCESS -> {
                showProgress(false)
                openSearchActivityAirport(
                    modelDashboard.dashboradAirportDomain.responseData as ArrayList<DashboardViewModel.AirportDataResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    AIRPORT_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.COUNTRY_SUCCESS -> {
                showProgress(false)
                openSearchActivityCountry(
                    modelDashboard.dashboradCountriesDomain.responseData as ArrayList<DashboardViewModel.CountriesResponse.CountriesResponseData>,
                    this.javaClass.simpleName.toString(),
                    LOCATION_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.LANG_SUCCESS -> {
                showProgress(false)
                openSearchActivityForLanguage(
                    modelDashboard.dashboradLangDomain.responseData as ArrayList<DashboardViewModel.LanguageResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    LANGUAGE_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.STATE_SUCCESS -> {
                showProgress(false)
                openSearchActivityForState(
                    modelDashboard.dashboradStateDomain.responseData as ArrayList<DashboardViewModel.StateResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    STATE_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.CITY_SUCCESS -> {
                showProgress(false)
                openSearchActivityForCity(
                    modelDashboard.dashboradCityDomain.responseData as ArrayList<DashboardViewModel.CityResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    CITY_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.ERROR
            -> {
                presenterDashboard.presentState(DashboardPresenter.MainView.ViewState.IDLE)
                showProgress(false)
                Utility.showCustomDialog(
                    this,
                    doRetrieveProfileModel().errorMessage,
                    "",
                    null
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
        title = resources.getString(R.string.personal_information)
        mainToolbar.setNavigationOnClickListener { onBackPressed() }
        init()
        setClickListeners()
        presenter.callAPIGetProfile(Pref.getValue(this, PrefConstants.USER_ID, "0").toString())
    }

    private fun setClickListeners() {
        update_button.setOnClickListener { attemptUpdate() }
        edt_country.setOnClickListener {
            presenterDashboard.callAPIGetCountry()
        }
        edt_lang.setOnClickListener {
            presenterDashboard.callAPIGetLanguage()
        }
        edt_time_zone.setOnClickListener {
            presenterDashboard.callAPITimeZone()
        }
        edt_home_airport.setOnClickListener {
            //presenterDashboard.callAPIAirports()
        }
        edt_state.setOnClickListener {
            //if (edt_country.text.toString().isNotEmpty())
            presenterDashboard.callAPIGetState(Pref.getValue(this, PrefConstants.PROFILE_COUNTRY_ID, "")!!)

        }
        edt_city.setOnClickListener {
            //if (edt_state.text.toString().isNotEmpty())
            presenterDashboard.callAPIGetCity(Pref.getValue(this, PrefConstants.STATE_ID, "")!!)
        }
    }

    private fun init() {
        this.model = ProfileViewModel(this)
        this.presenter = ProfilePresenterImpl(this, this)
        this.modelDashboard = DashboardViewModel(this)
        this.presenterDashboard = DashboardPresenterImpl(this, this)
    }

    override fun showState(viewState: ProfilePresenter.MainView.ViewState) {
        when (viewState) {
            IDLE -> showProgress(false)
            LOADING -> showProgress(true)
            SUCCESS -> {
                autoFillFields()
            }
            UPDATE_SUCCESS -> {
                onBackPressed()
            }
            ERROR
            -> {
                presenter.presentState(IDLE)
                showProgress(false)
                Utility.showCustomDialog(
                    this,
                    doRetrieveProfileModel().errorMessage,
                    "",
                    object : CustomDialogPresenter.CustomDialogView {
                        override fun onPositiveButtonClicked() {
                            presenter.presentState(UPDATE_SUCCESS)
                        }

                        override fun onNegativeButtonClicked() {
                        }
                    })
            }
        }
    }

    private fun autoFillFields() {
        edt_email.setText(model.profileDomain.responseData?.email)
        edt_first_name.setText(model.profileDomain.responseData?.firstName)
        edt_last_name.setText(model.profileDomain.responseData?.lastName)
        edt_phone_no.setText(model.profileDomain.responseData?.phoneNumber)
        edt_country.setText(model.profileDomain.responseData?.country)
        edt_state.setText(model.profileDomain.responseData?.state)
        edt_city.setText(model.profileDomain.responseData?.city)
        edt_home_airport.setText(model.profileDomain.responseData?.homeAirport)
        edt_time_zone.setText(model.profileDomain.responseData?.timezone)
        edt_lang.setText(model.profileDomain.responseData?.language)

        if (model.profileDomain.responseData?.promoChecked != 0)
            checkbox_promotions_account.isChecked = true
        else
            checkbox_promotions_account.isChecked = false
        if (null != model.profileDomain.responseData?.countryId)
            Pref.setValue(this, PrefConstants.PROFILE_COUNTRY_ID, model.profileDomain.responseData?.countryId!!)
        else
            Pref.setValue(this, PrefConstants.PROFILE_COUNTRY_ID, "")

        if (null != model.profileDomain.responseData?.stateId)
            Pref.setValue(this, PrefConstants.STATE_ID, model.profileDomain.responseData?.stateId!!)
        else
            Pref.setValue(this, PrefConstants.STATE_ID, "")
        presenter.presentState(ProfilePresenter.MainView.ViewState.IDLE)
    }

    override fun getLayoutResource(): Int {
        return R.layout.common_toolbar
    }

    private fun attemptUpdate() {
        edt_email.error = null
        edt_phone_no.error = null
        val emailStr = edt_email.text.toString()
        var check = if (!checkbox_promotions_account.isChecked) "0" else "1"
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(emailStr)) {
            edt_email.error = getString(R.string.error_field_required_email)
            focusView = edt_email
            cancel = true
        } else if (!Utility.isEmailValid(emailStr)) {
            edt_email.error = getString(R.string.error_invalid_email)
            focusView = edt_email
            cancel = true
        }
        checkbox_promotions_account.setOnCheckedChangeListener { _, isChecked ->
            check = if (isChecked) "0" else "1"
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            presenter.callAPIUpdateProfile(
                Pref.getValue(this, PrefConstants.USER_ID, "0")!!,
                edt_email.text.toString(),
                edt_first_name.text.toString(),
                edt_last_name.text.toString(),
                edt_phone_no.text.toString(),
                Pref.getValue(this, PrefConstants.PROFILE_AIRPORT_ID, "")!!,
                Pref.getValue(this, PrefConstants.PROFILE_COUNTRY_ID, "")!!,
                Pref.getValue(this, PrefConstants.STATE_ID, "")!!,
                Pref.getValue(this, PrefConstants.PROFILE_TIMEZONE_ID, "")!!,
                Pref.getValue(this, PrefConstants.CITY_ID, "")!!,
                Pref.getValue(this, PrefConstants.PROFILE_LANGUAGE_ID, "")!!,
                check
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    edt_country.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME).toString())
                    Pref.setValue(
                        this,
                        PrefConstants.PROFILE_COUNTRY_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
            STATE_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    edt_state.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME).toString())
                    Pref.setValue(
                        this,
                        PrefConstants.STATE_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
            CITY_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    edt_city.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME).toString())
                    Pref.setValue(
                        this,
                        PrefConstants.CITY_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
            LANGUAGE_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    edt_lang.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME).toString())
                    Pref.setValue(
                        this,
                        PrefConstants.PROFILE_LANGUAGE_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
            AIRPORT_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    edt_home_airport.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME).toString())
                    Pref.setValue(
                        this,
                        PrefConstants.PROFILE_AIRPORT_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
            TIMEZONE_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    edt_time_zone.setText(data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME).toString())
                    Pref.setValue(
                        this,
                        PrefConstants.PROFILE_TIMEZONE_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
        }
    }

    private fun openSearchActivityTimeZones(
        arrayListTimeZones: ArrayList<DashboardViewModel.TimeZoneResponse.ResponseData>,
        title: String,
        timeZonesSelectionRequest: Int
    ) {

        val intent = Intent(this, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.TIMEZONE_LIST_PROFILE, arrayListTimeZones)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, timeZonesSelectionRequest)
    }

    private fun openSearchActivityAirport(
        arrayListAirport: ArrayList<DashboardViewModel.AirportDataResponse.ResponseData>,
        title: String,
        airportSelectionRequest: Int
    ) {

        val intent = Intent(this, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.AIRPORT_LIST_PROFILE, arrayListAirport)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, airportSelectionRequest)
    }

    private fun openSearchActivityCountry(
        arrayListCountry: ArrayList<DashboardViewModel.CountriesResponse.CountriesResponseData>,
        title: String,
        locationSelectionRequest: Int
    ) {

        val intent = Intent(this, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.LOCATION_LIST_PROFILE, arrayListCountry)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, locationSelectionRequest)
    }

    private fun openSearchActivityForLanguage(
        arrayList: ArrayList<DashboardViewModel.LanguageResponse.ResponseData>,
        title: String,
        languageSelectionRequest: Int
    ) {
        val intent = Intent(this, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.LANGUAGE_LIST_PROFILE, arrayList)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, languageSelectionRequest)
    }

    private fun openSearchActivityForState(
        arrayList: ArrayList<DashboardViewModel.StateResponse.ResponseData>,
        title: String,
        languageSelectionRequest: Int
    ) {
        val intent = Intent(this, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.STATE_LIST_PROFILE, arrayList)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, languageSelectionRequest)
    }

    private fun openSearchActivityForCity(
        arrayList: ArrayList<DashboardViewModel.CityResponse.ResponseData>,
        title: String,
        languageSelectionRequest: Int
    ) {
        val intent = Intent(this, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.CITY_LIST_PROFILE, arrayList)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, languageSelectionRequest)
    }
}