package com.example.gcsalesapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.request.Mechanic
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.data.response.MechanicData
import com.example.gcsalesapp.repository.Utill
import kotlinx.android.synthetic.main.view_dealer_list_item.view.*
import kotlinx.android.synthetic.main.view_list_loader.view.*
import kotlinx.android.synthetic.main.view_mechanic_list_item.view.*
import javax.inject.Inject



class MechanicPaggingAdapter @Inject constructor(
    private val glide: RequestManager
) : PagingDataAdapter<MechanicData, RecyclerView.ViewHolder>(diffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<MechanicData>() {
            override fun areItemsTheSame(oldItem: MechanicData, newItem: MechanicData): Boolean {
                return oldItem.PhoneNo == newItem.PhoneNo
            }

            override fun areContentsTheSame(oldItem: MechanicData, newItem: MechanicData): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var engagementStatus: List<MechanicData>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    class DealerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class LoadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) MechanicPaggingAdapter.DealerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_mechanic_list_item,
                parent,
                false
            )
        ) else LoadViewHolder( LayoutInflater.from(parent.context).inflate(
            R.layout.view_list_loader,
            parent,
            false
        ))
    }


    private var loadMoreListener: (() -> Unit)? = null


    fun setOnLoadMoreListener(listener: () -> Unit) {
        loadMoreListener = listener
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            val engagement = engagementStatus[position]
            holder.itemView.apply {

                delete_img.visibility= INVISIBLE
                mech_dealer_name.visibility= VISIBLE
                mech_dealer_name.text = engagement.DealershipName
                mech_name.text="${engagement.FirstName} ${engagement.LastName}"
                mech_phone.text="${engagement.PhoneNo}"


            }
        }else{
            holder.itemView.apply {
                error_txt.visibility = if (state == State.LOADING) VISIBLE else View.INVISIBLE
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