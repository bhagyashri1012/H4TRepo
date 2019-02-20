package com.usit.hub4tickets.dashboard.ui.settingsimport android.app.Activityimport android.content.Intentimport android.os.Bundleimport android.support.v4.app.Fragmentimport android.support.v4.content.LocalBroadcastManagerimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.dashboard.model.DashboardViewModelimport com.usit.hub4tickets.dashboard.ui.DashboardActivityimport com.usit.hub4tickets.domain.presentation.presenters.DashboardPresenterimport com.usit.hub4tickets.domain.presentation.screens.main.DashboardPresenterImplimport com.usit.hub4tickets.search.CommonSearchActivityimport com.usit.hub4tickets.utils.Constantimport com.usit.hub4tickets.utils.Prefimport com.usit.hub4tickets.utils.PrefConstantsimport com.usit.hub4tickets.utils.Utilityimport kotlinx.android.synthetic.main.fragment_settings_panel.*/** * Created by Bhagyashri Burade * Date: 24/10/2018 * Email: bhagyashri.burade@usit.net.in */class SettingsFragment : Fragment(), DashboardPresenter.MainView {    private val LOCATION_SELECTION_REQUEST = 101    private val LANGUAGE_SELECTION_REQUEST = 102    private val CURRENCY_SELECTION_REQUEST = 103    private lateinit var model: DashboardViewModel    private lateinit var presenter: DashboardPresenter    override fun doRetrieveModel(): DashboardViewModel = this.model    override fun showState(viewState: DashboardPresenter.MainView.ViewState) {        when (viewState) {            DashboardPresenter.MainView.ViewState.IDLE -> Utility.showProgress(false, context)            DashboardPresenter.MainView.ViewState.LOADING -> Utility.showProgress(true, context)            DashboardPresenter.MainView.ViewState.SAVE_SUCCESS -> {                Pref.setValue(context, PrefConstants.IS_FIRST_TIME_ALLOW, true)                showState(DashboardPresenter.MainView.ViewState.IDLE)                activity?.supportFragmentManager?.beginTransaction()?.remove(this)                    ?.setCustomAnimations(R.anim.enter_from_left, R.anim.slide_to_right)?.hide(this)?.commit()                    var filter = "thisIsForMyFragment";                    val intent = Intent(filter); //If you need extra, add: intent.putExtra("extra","something");                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)            }            DashboardPresenter.MainView.ViewState.SUCCESS -> setData(                model.settingsDomain.responseData?.countryName,                model.settingsDomain.responseData?.languageName,                model.settingsDomain.responseData?.currencyName,                model.settingsDomain.responseData?.countryId.toString(),                model.settingsDomain.responseData?.languageId.toString(),                model.settingsDomain.responseData?.latestCurrencyId.toString()            )            DashboardPresenter.MainView.ViewState.COUNTRY_SUCCESS -> {                openSearchActivityCountry(                    model.dashboradCountriesDomain.responseData as ArrayList<DashboardViewModel.CountriesResponse.CountriesResponseData>,                    this.javaClass.simpleName.toString(),                    LOCATION_SELECTION_REQUEST                )            }            DashboardPresenter.MainView.ViewState.LANG_SUCCESS -> {                openSearchActivityForLanguage(                    model.dashboradLangDomain.responseData as ArrayList<DashboardViewModel.LanguageResponse.ResponseData>,                    this.javaClass.simpleName.toString(),                    LANGUAGE_SELECTION_REQUEST                )            }            DashboardPresenter.MainView.ViewState.CURRENCY_SUCCESS -> {                openSearchActivityForCurrency(                    model.dashboradCurrencyDomain.currencyData as ArrayList<DashboardViewModel.CurrencyResponse.CurrencyData>,                    this.javaClass.simpleName.toString(),                    CURRENCY_SELECTION_REQUEST                )            }            DashboardPresenter.MainView.ViewState.ERROR            -> {                presenter.presentState(DashboardPresenter.MainView.ViewState.IDLE)                if (!doRetrieveModel().errorMessage.message.equals("Please try again!"))                    Utility.showCustomDialog(context, doRetrieveModel().errorMessage.message, "", null)            }        }    }    override fun onCreateView(        inflater: LayoutInflater, container: ViewGroup?,        savedInstanceState: Bundle?    ): View? {        return inflater.inflate(R.layout.fragment_settings_panel, container, false)    }    override fun onActivityCreated(savedInstanceState: Bundle?) {        super.onActivityCreated(savedInstanceState)        init()        var location = ""        var lang = ""        if ((activity as DashboardActivity).checkPermissions() && !Pref.getValue(context,PrefConstants.IS_FIRST_TIME_ALLOW,false)) {            tv_settings_country_name.text = Pref.getValue(context, PrefConstants.DEFAULT_LOCATION, "United Kingdom")!!            tv_settings_currency_name.text = Pref.getValue(context, PrefConstants.CURRENCY_DEFAULT, "GBP")!!        } else {            if (Pref.getValue(context, PrefConstants.COUNTRY_ID, "").equals(""))                location = Pref.getValue(context, PrefConstants.DEFAULT_LOCATION, "")!!            else                location = Pref.getValue(context, PrefConstants.COUNTRY, "").toString()            if (Pref.getValue(context, PrefConstants.CURRENCY_ID, "").equals(""))                lang = Pref.getValue(context, PrefConstants.CURRENCY_DEFAULT, "")!!            else                lang = Pref.getValue(context, PrefConstants.CURRENCY, "").toString()            presenter.callAPIGetSettingsData(                Pref.getValue(context, PrefConstants.USER_ID, "0").toString(),                location,                lang            )            setData(                Pref.getValue(context, PrefConstants.COUNTRY, ""),                Pref.getValue(context, PrefConstants.LANGUAGE, ""),                Pref.getValue(context, PrefConstants.CURRENCY, ""),                Pref.getValue(context, PrefConstants.COUNTRY_ID, ""),                Pref.getValue(context, PrefConstants.LANGUAGE_ID, ""),                Pref.getValue(context, PrefConstants.CURRENCY_ID, "")            )        }    }    private fun setData(        countryName: String?,        languageName: String?,        currencyName: String?,        countryId: String?,        languageId: String?,        latestCurrencyId: String?    ) {        Pref.setValue(            context,            PrefConstants.COUNTRY_ID,            countryId!!        )        Pref.setValue(            context,            PrefConstants.LANGUAGE_ID,            "41"        )        /* Pref.setValue(             context,             PrefConstants.LANGUAGE_ID,             languageId!!         )*/        Pref.setValue(            context,            PrefConstants.CURRENCY_ID,            latestCurrencyId!!        )        tv_settings_country_name.text = countryName        tv_settings_language_name.text = languageName        tv_settings_currency_name.text = currencyName    }    private fun init() {        this.model = DashboardViewModel(context)        this.presenter = DashboardPresenterImpl(this, context)    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        cancel_button.setOnClickListener {            activity?.supportFragmentManager?.beginTransaction()?.remove(this)                ?.setCustomAnimations(R.anim.enter_from_left, R.anim.slide_to_right)?.hide(this)?.commit()        }        ll_settings_country.setOnClickListener {            presenter.callAPIGetCountry()        }        ll_settings_language.setOnClickListener {            //dynamic->            //presenter.callAPIGetLanguage()        }        //static for time being->        tv_settings_language_name.text = "English"        Pref.setValue(            context,            PrefConstants.LANGUAGE,            "English"        )        Pref.setValue(            context,            PrefConstants.LANGUAGE_ID,            "41"        )        ll_settings_currency.setOnClickListener {            presenter.callAPIGetCurrency()        }        settings_apply_button.setOnClickListener {            presenter.callAPISaveSettingsData(                Pref.getValue(                    context,                    PrefConstants.USER_ID,                    "0"                ).toString(),                Pref.getValue(context, PrefConstants.COUNTRY_ID, "").toString(),                Pref.getValue(context, PrefConstants.CURRENCY_ID, "").toString(),                Pref.getValue(context, PrefConstants.LANGUAGE_ID, "").toString()            )        }    }    private fun openSearchActivityCountry(        arrayListCountry: ArrayList<DashboardViewModel.CountriesResponse.CountriesResponseData>,        title: String,        locationSelectionRequest: Int    ) {        val intent = Intent(context, CommonSearchActivity::class.java)        intent.putParcelableArrayListExtra(Constant.Path.LOCATION_LIST, arrayListCountry)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)        startActivityForResult(intent, locationSelectionRequest)    }    private fun openSearchActivityForLanguage(        arrayList: ArrayList<DashboardViewModel.LanguageResponse.ResponseData>,        title: String,        languageSelectionRequest: Int    ) {        val intent = Intent(context, CommonSearchActivity::class.java)        intent.putParcelableArrayListExtra(Constant.Path.LANGUAGE_LIST, arrayList)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)        startActivityForResult(intent, languageSelectionRequest)    }    private fun openSearchActivityForCurrency(        arrayList: ArrayList<DashboardViewModel.CurrencyResponse.CurrencyData>,        title: String,        languageSelectionRequest: Int    ) {        val intent = Intent(context, CommonSearchActivity::class.java)        intent.putParcelableArrayListExtra(Constant.Path.CURRENCY_LIST, arrayList)        intent.putExtra(Constant.Path.ACTIVITY_TITLE, title)        startActivityForResult(intent, languageSelectionRequest)    }    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        super.onActivityResult(requestCode, resultCode, data)        when (requestCode) {            LOCATION_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    tv_settings_country_name.text = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)                    Pref.setValue(                        context,                        PrefConstants.COUNTRY,                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)!!                    )                    Pref.setValue(                        context,                        PrefConstants.COUNTRY_ID,                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!                    )                }            }            LANGUAGE_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    tv_settings_language_name.text = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)                    Pref.setValue(                        context,                        PrefConstants.LANGUAGE,                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)!!                    )                    Pref.setValue(                        context,                        PrefConstants.LANGUAGE_ID,                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!                    )                }            }            CURRENCY_SELECTION_REQUEST -> {                if (resultCode == Activity.RESULT_OK) {                    tv_settings_currency_name.text = data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)                    Pref.setValue(                        context,                        PrefConstants.CURRENCY,                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_NAME)!!                    )                    Pref.setValue(                        context,                        PrefConstants.CURRENCY_ID,                        data?.getStringExtra(PrefConstants.SELECTED_ITEMS_ID)!!                    )                }            }        }    }    companion object {        @JvmStatic        fun newInstance() =            SettingsFragment().apply {                arguments = Bundle().apply {                }            }    }}