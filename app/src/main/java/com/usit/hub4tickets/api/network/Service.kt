package com.usit.hub4tickets.domain.api.sampleimport com.usit.hub4tickets.dashboard.model.DashboardViewModelimport com.usit.hub4tickets.domain.presentation.screens.main.LoginViewModelimport com.usit.hub4tickets.domain.presentation.screens.main.ProfileViewModelimport com.usit.hub4tickets.domain.presentation.screens.main.SignUpViewModelimport com.usit.hub4tickets.flight.model.FlightViewModelimport io.reactivex.Flowableimport retrofit2.http.Bodyimport retrofit2.http.GETimport retrofit2.http.POSTimport retrofit2.http.Query/** * Created by Bhagyashri Burade * Date: 24/10/2018 * Email: bhagyashri.burade@usit.net.in */interface Service {    @POST("login")    fun getLogin(@Body loginDto: Login): Flowable<LoginViewModel.LoginResponse>    @POST("registeruser")    fun getRegistration(@Body signUpDto: SignUp): Flowable<SignUpViewModel.SignUpResponse>    @POST("otp")    fun sendOtp(@Body signUpDto: SendOtp): Flowable<LoginViewModel.LoginResponse>    @POST("otp/verify")    fun verifyOTP(@Body signUpDto: VerifyOTP): Flowable<LoginViewModel.LoginResponse>    @POST("resetpassword")    fun resetPassword(@Body resetPasswordDto: ResetPassword): Flowable<LoginViewModel.LoginResponse>    @GET("countries")    fun getCountries(): Flowable<DashboardViewModel.CountriesResponse>    @GET("langauges")    fun getLangauges(): Flowable<DashboardViewModel.LanguageResponse>    @GET("timezone")    fun getTimezone(): Flowable<DashboardViewModel.TimeZoneResponse>    @GET("airports")    fun getAirports(): Flowable<DashboardViewModel.AirportDataResponse>    @GET("currencies")    fun getCurrencies(): Flowable<DashboardViewModel.CurrencyResponse>    @POST("states")    fun getStates(@Body countryData: CountryData): Flowable<DashboardViewModel.StateResponse>    @POST("cities")    fun getCities(@Body stateData: StateData): Flowable<DashboardViewModel.CityResponse>    @POST("settingdata")    fun settingsData(@Body settingsData: SettingsData): Flowable<DashboardViewModel.SettingsResponse>    @POST("settingdata")    fun getSettingsDataWithoutUserId(@Body settingsData: SettingsData): Flowable<ProfileViewModel.SettingsResponse>    @POST("savesettingdata")    fun saveSettingsData(@Body settingsData: SaveSettingsData): Flowable<DashboardViewModel.SettingsResponse>    @POST("changepassword")    fun changePassword(@Body changePasswordDto: ChangePassword): Flowable<ProfileViewModel.ProfileResponse>    @POST("getprofile")    fun getProfileData(@Body profileData: ProfileData): Flowable<ProfileViewModel.ProfileResponse>    @POST("updateprofile")    fun updateProfileData(@Body updateProfileData: UpdateProfileData): Flowable<ProfileViewModel.ProfileResponse>    @GET("airports")    fun getAirportData(): Flowable<FlightViewModel.AirPortDataResponse>    // fun getAirportData(@Body airportData: AirportData): Flowable<FlightViewModel.AirPortDataResponse>    @POST("flights_multi_test")    fun getMultiCity(        @Body multiCityData: MultiCityData,        @Query("page") current_page: Int,        @Query("size") total_pages: Int    ): Flowable<FlightViewModel.MultiCityResponse>    @POST("flights-test")    fun getFlightDetails(        @Body flightData: FlightData,        @Query("page") current_page: Int,        @Query("size") total_pages: Int    ): Flowable<FlightViewModel.FlightListResponse>    @POST("savesettingdata")    fun setLocationSettingsData(@Body saveLocationData: SaveLocationData): Flowable<DashboardViewModel.SettingsResponse>}