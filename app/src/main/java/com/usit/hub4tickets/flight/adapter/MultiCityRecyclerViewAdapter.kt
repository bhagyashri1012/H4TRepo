package com.usit.hub4tickets.flight.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.usit.hub4tickets.R
import com.usit.hub4tickets.flight.model.FlightViewModel
import com.usit.hub4tickets.utils.pagination.PaginationAdapterCallback

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */

class MultiCityRecyclerViewAdapter(
    var items: ArrayList<FlightViewModel.ResponseDataMulticity>?,
    private val listener: MultiCityInnerAdapter.OnItemClickListener?,
    var totalPassengers: String,
    var price: String,
    var currency: String,
    var mCallback: PaginationAdapterCallback
) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // View Types
    private val ITEM = 0
    private val LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            ITEM -> {
                val viewItem =
                    LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_list_multicity, parent, false)
                viewHolder = TextItemViewHolderMulticity(viewItem)
            }
            LOADING -> {
                val viewLoading = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(viewLoading)
            }
        }
        return viewHolder!!
    }

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                (holder as TextItemViewHolderMulticity)
                holder.price.text = items!![position].currencySymbol + " " + items!![position].price.toString()
                val childLayoutManager = LinearLayoutManager(holder.rc.context, LinearLayout.VERTICAL, false)
                //childLayoutManager.initialPrefetchItemCount = 4
                holder.rc.apply {
                    layoutManager = childLayoutManager
                    adapter =
                        MultiCityInnerAdapter(
                            items!![position].multiCityResults,
                            listener,
                            totalPassengers,
                            items!![position].price.toString(),
                            items!![position].currencySymbol,
                            items!![position].deepLink
                        )
                    setRecycledViewPool(viewPool)
                }
            }
                LOADING ->
            if (retryPageLoad) {
                (holder as RecyclerViewAdapter.LoadingVH).mErrorLayout.visibility = View.VISIBLE
                (holder as RecyclerViewAdapter.LoadingVH).mProgressBar.visibility = View.GONE

                (holder as RecyclerViewAdapter.LoadingVH).mErrorTxt.text = if (errorMsg != null)
                    errorMsg
                else
                    "An unexpected error occurred"

            } else {
                (holder as RecyclerViewAdapter.LoadingVH).mErrorLayout.visibility = View.GONE
                (holder as RecyclerViewAdapter.LoadingVH).mProgressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    private var isLoadingAdded = false
    private var retryPageLoad = false
    private var errorMsg: String? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == items!!.size - 1 && isLoadingAdded)
            LOADING else
            ITEM
    }

    /*
           Helpers - Pagination
      _________________________________________________________________________________________________
       */

    fun add(r: FlightViewModel.ResponseDataMulticity) {
        items!!.add(r)
        notifyItemInserted(items!!.size - 1)
    }

    fun addAll(itemsResult: List<FlightViewModel.ResponseDataMulticity>) {
        for (result in itemsResult) {
            add(result)
        }
    }

    fun remove(r: FlightViewModel.ResponseDataMulticity) {
        val position = items!!.indexOf(r)
        if (position > -1) {
            items!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        //add(FlightViewModel.FlightListResponse.ResponseData())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = items!!.size - 1
        val result = getItem(position)

        if (result != null) {
            items!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): FlightViewModel.ResponseDataMulticity {
        return items!![position]
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(items!!.size - 1)

        if (errorMsg != null) this.errorMsg = errorMsg
    }

    inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mProgressBar: ProgressBar = itemView.findViewById(R.id.loadmore_progress)
        val mRetryBtn: ImageButton = itemView.findViewById(R.id.loadmore_retry)
        val mErrorTxt: TextView = itemView.findViewById(R.id.loadmore_errortxt)
        val mErrorLayout: LinearLayout = itemView.findViewById(R.id.loadmore_errorlayout)

        init {
            mRetryBtn.setOnClickListener(this)
            mErrorLayout.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.loadmore_retry, R.id.loadmore_errorlayout -> {

                    showRetry(false, null)
                    mCallback.retryPageLoad()
                }
            }
        }
    }
}
