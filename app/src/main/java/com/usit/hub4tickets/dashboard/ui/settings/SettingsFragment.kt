package com.usit.hub4tickets.dashboard.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.usit.hub4tickets.R
import com.usit.hub4tickets.dashboard.model.DashboardViewModel
import com.usit.hub4tickets.domain.presentation.presenters.DashboardPresenter
import com.usit.hub4tickets.domain.presentation.screens.main.DashboardPresenterImpl
import com.usit.hub4tickets.search.CommonSearchActivity
import com.usit.hub4tickets.utils.Constant
import com.usit.hub4tickets.utils.Pref
import com.usit.hub4tickets.utils.PrefConstants
import com.usit.hub4tickets.utils.Utility
import kotlinx.android.synthetic.main.fragment_settings_panel.*

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

class SettingsFragment : Fragment(), DashboardPresenter.MainView {

    private val LOCATION_SELECTION_REQUEST = 101
    private val LANGUAGE_SELECTION_REQUEST = 102
    private val CURRENCY_SELECTION_REQUEST = 103
    private lateinit var model: DashboardViewModel
    private lateinit var presenter: DashboardPresenter

    override fun doRetrieveModel(): DashboardViewModel = this.model
    override fun showState(viewState: DashboardPresenter.MainView.ViewState) {
        when (viewState) {
            DashboardPresenter.MainView.ViewState.IDLE -> showProgress(false)
            DashboardPresenter.MainView.ViewState.LOADING -> showProgress(true)
            DashboardPresenter.MainView.ViewState.SAVE_SUCCESS -> showProgress(false)
            DashboardPresenter.MainView.ViewState.SUCCESS -> setData(
                model.settingsDomain.responseData?.countryName,
                model.settingsDomain.responseData?.languageName,
                model.settingsDomain.responseData?.currencyName
            )
            DashboardPresenter.MainView.ViewState.COUNTRY_SUCCESS -> {
                showProgress(false)
                openSearchActivityCountry(
                    model.dashboradCountriesDomain.responseData as ArrayList<DashboardViewModel.CountriesResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    LOCATION_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.LANG_SUCCESS -> {
                showProgress(false)
                openSearchActivityForLanguage(
                    model.dashboradLangDomain.responseData as ArrayList<DashboardViewModel.LanguageResponse.ResponseData>,
                    this.javaClass.simpleName.toString(),
                    LANGUAGE_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.CURRENCY_SUCCESS -> {
                showProgress(false)
                openSearchActivityForCurrency(
                    model.dashboradCurrencyDomain.currencyData as ArrayList<DashboardViewModel.CurrencyResponse.CurrencyData>,
                    this.javaClass.simpleName.toString(),
                    CURRENCY_SELECTION_REQUEST
                )
            }
            DashboardPresenter.MainView.ViewState.ERROR
            -> {
                presenter.presentState(DashboardPresenter.MainView.ViewState.IDLE)
                Utility.showDialog(null, doRetrieveModel().errorMessage, context, activity)
            }
        }
    }

    private fun showProgress(show: Boolean) {
        if (show)
            Utility.showProgressDialog(context = context)
        else
            Utility.hideProgressBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_panel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        presenter.callAPIGetSettingsData(Pref.getValue(context, PrefConstants.USER_ID, "0").toString())
        setData(
            Pref.getValue(context, PrefConstants.COUNTRY, "India"),
            Pref.getValue(context, PrefConstants.LANGUAGE, "English"),
            Pref.getValue(context, PrefConstants.CURRENCY, "Indian Rupees")
        )
    }

    private fun setData(countryName: String?, languageName: String?, currencyName: String?) {
        tv_settings_country_name.text = countryName
        tv_settings_language_name.text = languageName
        tv_settings_currency_name.text = currencyName
    }

    private fun init() {
        this.model = DashboardViewModel(context)
        this.presenter = DashboardPresenterImpl(this, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancel_button.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
        }
        ll_settings_country.setOnClickListener {
            presenter.callAPIGetCountry()
        }

        ll_settings_language.setOnClickListener {
            presenter.callAPIGetLanguage()
        }

        ll_settings_currency.setOnClickListener {
            presenter.callAPIGetCurrency()
        }

        settings_apply_button.setOnClickListener {
            presenter.callAPISaveSettingsData(
                Pref.getValue(
                    context,
                    PrefConstants.USER_ID,
                    "0"
                ).toString(),
                Pref.getValue(context, PrefConstants.COUNTRY_ID, "").toString(),
                Pref.getValue(context, PrefConstants.CURRENCY_ID, "").toString(),
                Pref.getValue(context, PrefConstants.LANGUAGE_ID, "").toString()
            )
        }
    }

    private fun openSearchActivityCountry(
        arrayListCountry: ArrayList<DashboardViewModel.CountriesResponse.ResponseData>,
        title: String,
        locationSelectionRequest: Int
    ) {

        val intent = Intent(context, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.LOCATION_LIST, arrayListCountry)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, locationSelectionRequest)
    }

    private fun openSearchActivityForLanguage(
        arrayList: ArrayList<DashboardViewModel.LanguageResponse.ResponseData>,
        title: String,
        languageSelectionRequest: Int
    ) {
        val intent = Intent(context, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.LANGUAGE_LIST, arrayList)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, languageSelectionRequest)
    }

    private fun openSearchActivityForCurrency(
        arrayList: ArrayList<DashboardViewModel.CurrencyResponse.CurrencyData>,
        title: String,
        languageSelectionRequest: Int
    ) {
        val intent = Intent(context, CommonSearchActivity::class.java)
        intent.putParcelableArrayListExtra(Constant.Path.CURRENCY_LIST, arrayList)
        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)
        startActivityForResult(intent, languageSelectionRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_settings_country_name.text = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)
                    Pref.setValue(
                        context,
                        PrefConstants.COUNTRY,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)!!
                    )
                    Pref.setValue(
                        context,
                        PrefConstants.COUNTRY_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
            LANGUAGE_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_settings_language_name.text = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)
                    Pref.setValue(
                        context,
                        PrefConstants.LANGUAGE,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)!!
                    )
                    Pref.setValue(
                        context,
                        PrefConstants.LANGUAGE_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
            CURRENCY_SELECTION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    tv_settings_currency_name.text = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)
                    Pref.setValue(
                        context,
                        PrefConstants.CURRENCY,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)!!
                    )
                    Pref.setValue(
                        context,
                        PrefConstants.CURRENCY_ID,
                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!
                    )
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}