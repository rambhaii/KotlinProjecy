package com.example.gcsalesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.request.Mechanic
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.data.response.DealerOrderItem
import com.example.gcsalesapp.repository.Utill
import kotlinx.android.synthetic.main.view_dealer_list_item.view.*
import kotlinx.android.synthetic.main.view_mechanic_list_item.view.*
import kotlinx.android.synthetic.main.view_order_list_item.view.*
import javax.inject.Inject

class MechanicAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<MechanicAdapter.MechanicViewHolder>() {


    class MechanicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Mechanic>() {
        override fun areItemsTheSame(oldItem: Mechanic, newItem: Mechanic): Boolean {
            return oldItem.phoneNo == newItem.phoneNo
        }

        override fun areContentsTheSame(oldItem: Mechanic, newItem: Mechanic): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var engagementStatus: List<Mechanic>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MechanicViewHolder {
        return MechanicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_mechanic_list_item,
                parent,
                false
            )
        )
    }


    private var OnDeleteClickListener: ((Mechanic) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Mechanic) -> Unit) {
        OnDeleteClickListener = listener
    }


    override fun onBindViewHolder(holder: MechanicViewHolder, position: Int) {
        val engagement = engagementStatus[position]
        holder.itemView.apply {

            mech_name.text="${engagement.firstName} ${engagement.lastName}"
            mech_phone.text = engagement.phoneNo
            mech_type.text = engagement.MechanicVehicleTypeName

            delete_img.setOnClickListener {
                OnDeleteClickListener?.let { click ->
                    click(engagement)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return engagementStatus.size
    }

}