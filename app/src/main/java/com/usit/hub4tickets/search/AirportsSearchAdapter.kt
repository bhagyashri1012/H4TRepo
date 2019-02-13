package com.usit.hub4tickets.searchimport android.support.v7.widget.RecyclerViewimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport android.widget.Filterimport android.widget.Filterableimport com.usit.hub4tickets.Rimport com.usit.hub4tickets.flight.adapter.TextItemViewHolderForCommonSearchimport com.usit.hub4tickets.search.model.AirportsSelectorPojo/** * Created by Bhagyashri Burade * Date: 24/10/2018 * Email: bhagyashri.burade@usit.net.in */class AirportsSearchAdapter(    private var strActivityTitle: String?,    listItems: ArrayList<AirportsSelectorPojo>,    private var listener: OnItemClickListener) : RecyclerView.Adapter<TextItemViewHolderForCommonSearch>(), Filterable {    private var arrayListCommonSelector: ArrayList<AirportsSelectorPojo> = listItems    var temArrayListCommonSelector = listItems    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolderForCommonSearch {        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_common_serarch_adapter, parent, false)        return TextItemViewHolderForCommonSearch(view)    }    override fun onBindViewHolder(holder: TextItemViewHolderForCommonSearch, position: Int) {        if (strActivityTitle.equals("FragmentReturn"))            holder.textCountryView.visibility = View.VISIBLE        else if (strActivityTitle.equals("FragmentOneWay"))            holder.textCountryView.visibility = View.VISIBLE        else            holder.textCountryView.visibility = View.GONE        holder.bind(            temArrayListCommonSelector[position].airPortNameAndCode.toString(),            temArrayListCommonSelector[position].airPortCode.toString()        )        holder.itemView.setOnClickListener { listener.onListItemClick(temArrayListCommonSelector[position]) }    }    override fun getItemCount(): Int {        return if (arrayListCommonSelector != null) temArrayListCommonSelector.size else 0    }    var filter: CustomFilterAirports? = null    override fun getFilter(): Filter {        if (filter == null) {            filter = CustomFilterAirports(arrayListCommonSelector, this)        }        return filter as CustomFilterAirports    }    fun filter(text: String) {        if (text.isEmpty()) {            temArrayListCommonSelector.clear()            temArrayListCommonSelector.addAll(arrayListCommonSelector)        } else {            temArrayListCommonSelector.clear()            for (item in arrayListCommonSelector) {                if (item.airPortNameAndCode?.toLowerCase()?.startsWith(text)!!) {                    temArrayListCommonSelector.add(item)                }            }        }        if (temArrayListCommonSelector.isEmpty()) {            listener.onNoData(true)        } else {            listener.onNoData(false)        }        notifyDataSetChanged()    }    interface OnItemClickListener {        fun onListItemClick(commonSelectorPojo: AirportsSelectorPojo)        fun onNoData(isVisible: Boolean)    }}