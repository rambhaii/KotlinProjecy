package com.example.gcsalesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.response.DealerOrderItem
import com.example.gcsalesapp.repository.Utill
import kotlinx.android.synthetic.main.view_order_list_item.view.*
import javax.inject.Inject

class DealerOrderAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<DealerOrderAdapter.DealerOrderViewHolder>() {


    class DealerOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<DealerOrderItem>() {
        override fun areItemsTheSame(oldItem: DealerOrderItem, newItem: DealerOrderItem): Boolean {
            return oldItem.OrderID == newItem.OrderID
        }

        override fun areContentsTheSame(oldItem: DealerOrderItem, newItem: DealerOrderItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var engagementStatus: List<DealerOrderItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerOrderViewHolder {
        return DealerOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_order_list_item,
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: DealerOrderViewHolder, position: Int) {
        val engagement = engagementStatus[position]
        holder.itemView.apply {

            order_id.text = "#${engagement.OrderID.toInt()}"
            order_date.text = "Date: ${Utill.getFormatedDate(engagement.OrderDate,"yyyy-MM-dd","dd-LLL-yyyy")}"
            order_status.text =
                "${engagement.OrderStatusName}"
            order_vol.text =
                "${engagement.VolInLit} Lit."
        }
    }

    override fun getItemCount(): Int {
        return engagementStatus.size
    }

}