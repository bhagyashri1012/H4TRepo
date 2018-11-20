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
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : Fragment(), DashboardPresenter.MainView {
    private var param1: String? = null
    private var param2: String? = null
    private val LOCATION_SELECTION_REQUEST = 101
    private lateinit var model: DashboardViewModel
    private lateinit var presenter: DashboardPresenter
    override fun doRetrieveModel(): DashboardViewModel = this.model
    override fun showState(viewState: DashboardPresenter.MainView.ViewState) {
        when (viewState) {
            DashboardPresenter.MainView.ViewState.IDLE -> showProgress(false)
            DashboardPresenter.MainView.ViewState.LOADING -> showProgress(true)
            DashboardPresenter.MainView.ViewState.COUNTRY_SUCCESS -> {
                showProgress(false)
                openSearchActivityCountry(
                    model.dashboradCountriesDomain.responseData as ArrayList<DashboardViewModel.CountriesResponse.ResponseData>,
                    "SettingsFragment",
                    LOCATION_SELECTION_REQUEST
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_panel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        setData()
    }

    private fun setData() {
        tv_settings_country_name.text = Pref.getValue(context, PrefConstants.COUNTRY, "India")
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
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
