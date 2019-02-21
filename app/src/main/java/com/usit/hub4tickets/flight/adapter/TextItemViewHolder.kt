package com.usit.hub4tickets.flight.adapterimport android.content.Contextimport android.content.Intentimport android.support.v7.widget.RecyclerViewimport android.view.Viewimport android.widget.ImageViewimport android.widget.TextViewimport com.bumptech.glide.Glideimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.flight.model.FlightViewModelimport com.usit.hub4tickets.flight.ui.SeeDetailsActivityimport com.usit.hub4tickets.flight.ui.SeeDetailsMulticityActivityimport com.usit.hub4tickets.utils.Constantclass TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {    private val textView: TextView = itemView.findViewById(R.id.list_item) as TextView    fun bind(text: String) {        textView.text = text    }}class TextItemViewHolderForCommonSearch(itemView: View) : RecyclerView.ViewHolder(itemView) {    private val textView: TextView = itemView.findViewById(R.id.list_item) as TextView    val textCountryView: TextView = itemView.findViewById(R.id.list_sub_item) as TextView    fun bind(text: String, country: String) {        textView.text = text        textCountryView.text = country    }}class TextItemViewForTripDetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {    private val tripTitle: TextView = itemView.findViewById(R.id.trip_title) as TextView    private val tripDate: TextView = itemView.findViewById(R.id.trip_date) as TextView    private val tripTime: TextView = itemView.findViewById(R.id.tv_time) as TextView    private val tripDestinatn: TextView = itemView.findViewById(R.id.tv_destination) as TextView    //private val tripDuration: TextView = itemView.findViewById(R.id.tv_duration) as TextView    private val tripAirline: TextView = itemView.findViewById(R.id.tv_airline) as TextView    private val actionSeeDetails: TextView = itemView.findViewById(R.id.action_see_details) as TextView    private val firstStop: TextView = itemView.findViewById(R.id.txt_from_city) as TextView    private val lastStop: TextView = itemView.findViewById(R.id.txt_to_city) as TextView    private val stopDuration: TextView = itemView.findViewById(R.id.tv_stop_duration) as TextView    private val imageFirstStop: ImageView = itemView.findViewById(R.id.imv_firstStop) as ImageView    private val imageLastStop: ImageView = itemView.findViewById(R.id.imv_lastStop) as ImageView    private val stopCount: TextView = itemView.findViewById(R.id.stop_count) as TextView    //private val rvStopDetails: RecyclerView = itemView.findViewById(R.id.recycler_view_stop_details) as RecyclerView    fun bind(tripDetailsResponse: FlightViewModel.TripAllDetails, context: Context?) {        if (null != tripDetailsResponse) {            tripTitle.text = tripDetailsResponse?.fromCity + " - " +                    tripDetailsResponse?.toCity            tripAirline.text = tripDetailsResponse.airline            tripDate.text = tripDetailsResponse?.startDate            tripTime.text = tripDetailsResponse?.startTime + " - " +                    tripDetailsResponse?.endTime            tripDestinatn.text = tripDetailsResponse?.startAirPortName + " - " +                    tripDetailsResponse?.endAirPortName            if (tripDetailsResponse?.stopCount.equals("0"))                stopCount.text = "Direct"            else if (tripDetailsResponse?.stopCount.equals("1"))                stopCount.text = tripDetailsResponse?.stopCount.toString() + " Stop"            else                stopCount.text = tripDetailsResponse?.stopCount.toString() + " Stops"            // tripDuration.text = tripDetailsResponse?.duration            firstStop.text = tripDetailsResponse.startTime            lastStop.text = tripDetailsResponse.endTime            stopDuration.text = tripDetailsResponse.duration            Glide.with(itemView.context).load(tripDetailsResponse.imgUrl)                .into(imageFirstStop)            if (null != tripDetailsResponse.stopDetailsOutBound) {                Glide.with(itemView.context)                    .load(tripDetailsResponse.stopDetailsOutBound.get(tripDetailsResponse.stopDetailsOutBound.lastIndex)?.imgUrl)                    .into(imageLastStop)            } else                if (null != tripDetailsResponse.stopDetailsInBound) {                    Glide.with(itemView.context)                        .load(tripDetailsResponse.stopDetailsInBound.get(tripDetailsResponse.stopDetailsInBound.lastIndex)?.imgUrl)                        .into(imageLastStop)                }            actionSeeDetails.setOnClickListener {                val intent = Intent(context, SeeDetailsActivity::class.java)                intent.putExtra(Constant.Path.STOP_DETAILS, tripDetailsResponse)                context?.startActivity(intent)            }        }    }    fun bindMulticity(tripDetailsResponse: FlightViewModel.MultiCityResult, context: Context?) {        if (null != tripDetailsResponse) {            tripTitle.text = tripDetailsResponse?.fromCity + " - " +                    tripDetailsResponse?.toCity            tripDate.text = tripDetailsResponse?.startDate            tripTime.text = tripDetailsResponse?.startTime + " - " +                    tripDetailsResponse?.endTime            tripDestinatn.text = tripDetailsResponse?.startAirPortName + " - " +                    tripDetailsResponse?.endAirPortName            if (tripDetailsResponse?.stopCount == 0)                stopCount.text = "Direct"            else if (tripDetailsResponse?.stopCount == 1)                stopCount.text = tripDetailsResponse?.stopCount.toString() + " Stop"            else                stopCount.text = tripDetailsResponse?.stopCount.toString() + " Stops"            // tripDuration.text = tripDetailsResponse?.duration            tripAirline.text = tripDetailsResponse.airline            firstStop.text = tripDetailsResponse.startTime            lastStop.text = tripDetailsResponse.endTime            stopDuration.text = tripDetailsResponse.duration            Glide.with(itemView.context).load(tripDetailsResponse.imgUrl)                .into(imageFirstStop)            actionSeeDetails.setOnClickListener {                val intent = Intent(context, SeeDetailsMulticityActivity::class.java)                intent.putExtra(Constant.Path.STOP_DETAILS, tripDetailsResponse)                context?.startActivity(intent)            }        }    }}class TextItemViewForStopDetailsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {    private val imvAirline: ImageView = itemView.findViewById(R.id.imv_airline) as ImageView    private val tripTitle: TextView = itemView.findViewById(R.id.trip_title) as TextView    private val txtAirlineNo: TextView = itemView.findViewById(R.id.txt_airline) as TextView    private val tvCode: TextView = itemView.findViewById(R.id.tv_code) as TextView    private val tvOutBoundCode: TextView = itemView.findViewById(R.id.tv_out_bound_code) as TextView    private val tvDestination: TextView = itemView.findViewById(R.id.tv_destination) as TextView    private val tvTime: TextView = itemView.findViewById(R.id.tv_time) as TextView    private val tvOutBoundDestination: TextView = itemView.findViewById(R.id.tv_out_bound_destination) as TextView    private val tvOutBoundTime: TextView = itemView.findViewById(R.id.tv_out_bound_time) as TextView    private val tvDate: TextView = itemView.findViewById(R.id.tv_date) as TextView    private val tvOutBoundDate: TextView = itemView.findViewById(R.id.tv_out_bound_date) as TextView    private val tvStopDuration: TextView = itemView.findViewById(R.id.tv_waiting_duration) as TextView    private val tvLayover: TextView = itemView.findViewById(R.id.tv_layover) as TextView    fun bindStopDetails(        stopDetail: FlightViewModel.StopDetail?,        position: Int,        size: Int    ) {        if (null != stopDetail) {            Glide.with(itemView.context)                .load(stopDetail.imgUrl)                .into(imvAirline)            tripTitle.text = stopDetail.airline            txtAirlineNo.text = "Flight No : " + stopDetail.flightNo            tvDestination.text = stopDetail.fromCity            tvTime.text = stopDetail.startTime            tvOutBoundDestination.text = stopDetail.toCity            tvOutBoundTime.text = stopDetail.endTime            tvDate.text = stopDetail.startDate            tvOutBoundDate.text = stopDetail.endDate            tvCode.text = stopDetail.startAirportShortName            tvOutBoundCode.text = stopDetail.endAirportShortName            tvStopDuration.text = stopDetail?.duration            if (position == size - 1) {                tvLayover.visibility = View.GONE            } else {                tvLayover.visibility = View.VISIBLE                tvLayover.text = "Layover : " + stopDetail?.waitingDuration            }        }    }    fun bindStopDetailsMulticity(        stopDetail: FlightViewModel.StopDetailMulticity?,        position: Int,        size: Int    ) {        if (null != stopDetail) {            Glide.with(itemView.context)                .load(stopDetail.imgUrl)                .into(imvAirline)            tripTitle.text = stopDetail.airline            txtAirlineNo.text = "Flight No : " + stopDetail.flightNo            tvDestination.text = stopDetail.fromCity            tvTime.text = stopDetail.startTime            tvOutBoundDestination.text = stopDetail.toCity            tvOutBoundTime.text = stopDetail.endTime            tvDate.text = stopDetail.startDate            tvOutBoundDate.text = stopDetail.endDate            tvCode.text = stopDetail.startAirportShortName            tvOutBoundCode.text = stopDetail.endAirportShortName            tvStopDuration.text = stopDetail?.duration            if (position == size - 1) {                tvLayover.visibility = View.GONE            } else {                tvLayover.visibility = View.VISIBLE                tvLayover.text = "Layover : " + stopDetail?.waitingDuration            }        }    }}class TextItemViewHolderForArray(itemView: View) : RecyclerView.ViewHolder(itemView) {    private val textView: TextView = itemView.findViewById(R.id.list_item) as TextView    private val tvTime: TextView = itemView.findViewById(R.id.tv_time) as TextView    private val tvDestination: TextView = itemView.findViewById(R.id.tv_destination) as TextView    private val tvDuration: TextView = itemView.findViewById(R.id.tv_duration) as TextView    private val tvDurationOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_duration) as TextView    private val tvTimeOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_time) as TextView    private val tvDestinationOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_destination) as TextView    private val tvStopCountOutBound: TextView = itemView.findViewById(R.id.stop_count_outbound) as TextView    private val tvStopCount: TextView = itemView.findViewById(R.id.stop_count) as TextView    private val imvInBound: ImageView = itemView.findViewById(R.id.imv_inbound) as ImageView    private val imvOutBound: ImageView = itemView.findViewById(R.id.imv_out_bound) as ImageView    private val divider: View = itemView.findViewById(R.id.divider) as View    fun bind(        responseData: FlightViewModel.FlightListResponse.ResponseData,        className: String?    ) {        //out bound        if (null != responseData.outbondFlightDetails) {            tvDurationOutBound.text = responseData.outbondFlightDetails?.duration            tvTimeOutBound.text = responseData.outbondFlightDetails?.startTime + " - " +                    responseData.outbondFlightDetails?.endTime            tvDestinationOutBound.text = responseData.outbondFlightDetails?.startAirPortName + " - " +                    responseData.outbondFlightDetails?.endAirPortName + " , " +                    responseData.outbondFlightDetails?.airline            if (responseData.outbondFlightDetails?.stopCount == 0)                tvStopCountOutBound.text = "Direct"            else if (responseData.outbondFlightDetails?.stopCount == 1)                tvStopCountOutBound.text = responseData.outbondFlightDetails?.stopCount.toString() + " Stop"            else                tvStopCountOutBound.text = responseData.outbondFlightDetails?.stopCount.toString() + " Stops"            Glide.with(itemView.context).load(responseData.outbondFlightDetails?.imgUrl).into(imvOutBound)            textView.text = responseData.currency + "  " + responseData.price?.toString()        }        if (className?.equals("FragmentReturn")!!) {            if (null != responseData.inbondFlightDetails) {                tvTime.text = responseData.inbondFlightDetails?.startTime + " - " +                        responseData.inbondFlightDetails?.endTime                tvDestination.text = responseData.inbondFlightDetails?.startAirPortName + " - " +                        responseData.inbondFlightDetails?.endAirPortName + " , " +                        responseData.inbondFlightDetails?.airline                tvDuration.text = responseData.inbondFlightDetails?.duration                if (responseData.inbondFlightDetails?.stopCount == 0)                    tvStopCount.text = "Direct"                else if (responseData.inbondFlightDetails?.stopCount == 1)                    tvStopCount.text = responseData.inbondFlightDetails?.stopCount.toString() + " Stop"                else                    tvStopCount.text = responseData.inbondFlightDetails?.stopCount.toString() + " Stops"                Glide.with(itemView.context).load(responseData.inbondFlightDetails?.imgUrl).into(imvInBound)            } else {                hideInboundView()            }        } else {            hideInboundView()        }    }    private fun hideInboundView() {        tvTime.visibility = View.GONE        tvDestination.visibility = View.GONE        tvDuration.visibility = View.GONE        imvInBound.visibility = View.GONE        tvStopCount.visibility = View.GONE        divider.visibility = View.GONE    }}class TextItemViewHolderMulticity(itemView: View) : RecyclerView.ViewHolder(itemView) {    val rc: RecyclerView = itemView.findViewById(R.id.recycler_view_mc) as RecyclerView    val price: TextView = itemView.findViewById(R.id.list_item_price) as TextView}class TextItemViewHolderForMulticity(itemView: View) : RecyclerView.ViewHolder(itemView) {    private val tvDurationOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_duration) as TextView    private val tvTimeOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_time) as TextView    private val tvDestinationOutBound: TextView = itemView.findViewById(R.id.tv_out_bound_destination) as TextView    private val tvStopCountOutBound: TextView = itemView.findViewById(R.id.stop_count_outbound) as TextView    private val imvOutBound: ImageView = itemView.findViewById(R.id.imv_out_bound) as ImageView    fun bindMulticity(        responseData: FlightViewModel.MultiCityResult,        priceandcurr: String    ) {        tvDurationOutBound.text = responseData?.duration        tvTimeOutBound.text = responseData?.startTime + " - " +                responseData?.endTime        tvDestinationOutBound.text = responseData?.startAirPortName + " - " +                responseData?.endAirPortName + " , " +                responseData?.airline        if (responseData?.stopCount == 0)            tvStopCountOutBound.text = "Direct"        else if (responseData?.stopCount == 1)            tvStopCountOutBound.text = responseData?.stopCount.toString() + " Stop"        else            tvStopCountOutBound.text = responseData?.stopCount.toString() + " Stops"        Glide.with(itemView.context).load(responseData?.imgUrl).into(imvOutBound)    }}