package com.usit.hub4tickets.flight.adapterimport android.content.Contextimport android.support.v7.widget.RecyclerViewimport android.view.LayoutInflaterimport android.view.ViewGroupimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.flight.model.FlightViewModel/** * Created by anupamchugh on 05/10/16. *///190class MulticityTripDetailsViewAdapter(    private var items: List<FlightViewModel.MultiCityResult>?,    listener: Nothing?,    private var context: Context,    private var currencySymbol: String?) :    RecyclerView.Adapter<TextItemViewForTripDetailsHolder>() {    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewForTripDetailsHolder {        val view =            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_trip_details_list_item, parent, false)        return TextItemViewForTripDetailsHolder(view)    }    override fun onBindViewHolder(holder: TextItemViewForTripDetailsHolder, position: Int) {        holder.bindMulticity(items!![position], context, currencySymbol)    }    override fun getItemId(position: Int): Long {        return position.toLong()    }    override fun getItemCount(): Int {        return items!!.size    }}