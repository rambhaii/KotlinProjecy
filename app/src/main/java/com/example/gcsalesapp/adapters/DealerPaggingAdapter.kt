package com.example.gcsalesapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.repository.Utill
import kotlinx.android.synthetic.main.view_dealer_list_item.view.*
import kotlinx.android.synthetic.main.view_list_loader.view.*
import javax.inject.Inject


enum class State {
    DONE, LOADING, ERROR
}

class DealerPaggingAdapter @Inject constructor(
    private val glide: RequestManager
) : PagingDataAdapter<Dealer, RecyclerView.ViewHolder>(diffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Dealer>() {
            override fun areItemsTheSame(oldItem: Dealer, newItem: Dealer): Boolean {
                return oldItem.DealerID == newItem.DealerID
            }

            override fun areContentsTheSame(oldItem: Dealer, newItem: Dealer): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var engagementStatus: List<Dealer>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    class DealerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class LoadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) DealerPaggingAdapter.DealerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_dealer_list_item,
                parent,
                false
            )
        ) else LoadViewHolder( LayoutInflater.from(parent.context).inflate(
            R.layout.view_list_loader,
            parent,
            false
        ))
    }

    private var OnInteractionClickListener: ((Dealer) -> Unit)? = null
    private var OnOrderClickListener: ((Dealer) -> Unit)? = null
    private var onItemClickListener: ((Dealer) -> Unit)? = null
    private var loadMoreListener: (() -> Unit)? = null

    fun setOnItemClickListener(listener: (Dealer) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnInteractionClickListener(listener: (Dealer) -> Unit) {
        OnInteractionClickListener = listener
    }

    fun setOnOrderClickListener(listener: (Dealer) -> Unit) {
        OnOrderClickListener = listener
    }

    fun setOnLoadMoreListener(listener: () -> Unit) {
        loadMoreListener = listener
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            val engagement = engagementStatus[position]
            holder.itemView.apply {

                dealer_name.text = engagement.DealershipName
                dealer_type.text = engagement.DealershipType
                dealer_contact.text =
                    "${engagement.PhoneNo}"
                dealer_address.text =
                    "${engagement.District}-${engagement.PINCode}"
                dealer_avg.text =
                    if (engagement!!.CalculatedVolPerMonth > 0) "(${engagement!!.CalculatedVolPerMonth} Lit.)" else "(${engagement!!.EstimatedVolPerMonth} Lit.)"

                visit_count.text = "${engagement.VisitCount}"

                val bgColor =
                    Color.parseColor("#FF${engagement.EngagementStatusColourCode.replace("#", "")}")

                cardView.setCardBackgroundColor(bgColor)

                if(Utill.isColorDark(bgColor))
                {
                    visit_count.setTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
                }else{
                    visit_count.setTextColor(ResourcesCompat.getColor(context.resources, R.color.blackTxt, null))
                }

                if(engagement.VisitCount>0)
                {
                    visit_count.visibility  = VISIBLE
                }else{
                    visit_count.visibility  = GONE
                }


                dealer_ordered.text = "${engagement.MonthsVolumeInLit} Lit. Ordered"

                if (!engagement.EngagementStatusIMgUrl.isEmpty()) {
                    glide.load("${Constants.BASE_URL}${engagement.EngagementStatusIMgUrl}")
                        .into(no_engage_img)
                    no_engage_img.visibility = View.VISIBLE
                    dealer_ordered.visibility = View.GONE
                    order_engage_img.visibility = View.GONE

                } else {
                    if (engagement.OrderColorCode != null && !engagement.OrderColorCode.isEmpty()) {
//                    no_engage_img.setImageResource(R.drawable.ic_baseline_star_24)
                        val starColor =
                            Color.parseColor("#FF${engagement.OrderColorCode.replace("#", "")}")

                        order_engage_img.setColorFilter(starColor)
                        order_engage_img.visibility = View.VISIBLE
                    } else {
                        order_engage_img.visibility = View.GONE
                    }
//
                    no_engage_img.visibility = View.GONE
                    dealer_ordered.visibility = View.VISIBLE
                }

                cardView.setOnClickListener {
                    if (engagement.OpenVisitPage) {
                        OnInteractionClickListener?.let { click ->
                            click(engagement)
                        }
                    }
                }

                dealer_ordered.setOnClickListener {
                    OnOrderClickListener?.let { click ->
                        click(engagement)
                    }
                }

                setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(engagement)
                    }
                }


            }
        }else{
            holder.itemView.apply {
                error_txt.visibility = if (state == State.LOADING) VISIBLE else View.INVISIBLE
//                error_txt.visibility = if (state == State.ERROR) VISIBLE else View.INVISIBLE
                if(state == State.LOADING)
                {
                    loadMoreListener?.let { click ->
                        click()
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < engagementStatus.size) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }


    override fun getItemCount(): Int {
        return engagementStatus.size + if (hasFooter()) 1 else 0
    }




    private fun hasFooter(): Boolean {
        return engagementStatus.size != 0 && (state == State.LOADING || state == State.ERROR)
    }


    fun setState(state: State) {
        this.state = state
        notifyDataSetChanged()
//        notifyItemChanged(engagementStatus.size)
    }

}