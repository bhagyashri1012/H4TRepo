package com.usit.hub4tickets.flight.adapterimport android.support.v7.widget.RecyclerViewimport android.view.LayoutInflaterimport android.view.ViewGroupimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.flight.model.FlightViewModel/** * Created by Bhagyashri Burade * Date: 24/10/2018 * Email: bhagyashri.burade@usit.net.in */class MultiCityInnerAdapter(    var items: kotlin.collections.List<com.usit.hub4tickets.flight.model.FlightViewModel.MultiCityResult>,    private val listener: com.usit.hub4tickets.flight.adapter.MultiCityInnerAdapter.OnItemClickListener?,    var totalPassengers: kotlin.String,    var price: kotlin.String,    var currency: kotlin.String,    var deepLink: kotlin.String) : RecyclerView.Adapter<TextItemViewHolderForMulticity>() {    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolderForMulticity {        val view =            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_list_multicity_item, parent, false)        return TextItemViewHolderForMulticity(view)    }    override fun onBindViewHolder(holder: TextItemViewHolderForMulticity, position: Int) {        holder.bindMulticity(items!![position], currency + " " + price?.toString())        holder.itemView.setOnClickListener { listener?.onMulticityRowClick(items, totalPassengers, price,deepLink) }    }    override fun getItemId(position: Int): Long {        return position.toLong()    }    interface OnItemClickListener {        fun onMulticityRowClick(            responseData: List<FlightViewModel.MultiCityResult>,            totalPassengers: String,            price: String,            deepLink: String        )    }    override fun getItemCount(): Int {        return items!!.size    }}