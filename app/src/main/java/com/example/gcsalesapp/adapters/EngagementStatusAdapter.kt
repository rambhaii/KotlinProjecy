package com.example.gcsalesapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.response.Summary
import com.example.gcsalesapp.repository.Utill
import kotlinx.android.synthetic.main.dialog_message.view.*
import kotlinx.android.synthetic.main.dialog_progress.view.*
import kotlinx.android.synthetic.main.view_engagement_status.view.*
import javax.inject.Inject

class EngagementStatusAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<EngagementStatusAdapter.EngagementStatusViewHolder>() {


    class EngagementStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Summary>() {
        override fun areItemsTheSame(oldItem: Summary, newItem: Summary): Boolean {
            return oldItem.EngagementStatusID == newItem.EngagementStatusID
        }

        override fun areContentsTheSame(oldItem: Summary, newItem: Summary): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var engagementStatus: List<Summary>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    var totalDealer=0
//    get() = totalDealer
//    set(value) = totalDealer



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngagementStatusViewHolder {
        return EngagementStatusViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_engagement_status,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Summary) -> Unit)? = null

    fun setOnItemClickListener(listener: (Summary) -> Unit) {
        onItemClickListener = listener
    }


    override fun onBindViewHolder(holder: EngagementStatusViewHolder, position: Int) {
        val engagement = engagementStatus[position]
        holder.itemView.apply {

//            Utill.setColorFilter(engage_ctx.background, ResourcesCompat.getColor(context.resources, R.color.green, null))

            val bgColor = Color.parseColor("#FF${engagement.EngagementStatusColourCode.replace("#","")}")

            engage_ctx.setBackgroundColor(bgColor)

            if(Utill.isColorDark(bgColor))
            {
//                engage_img_txt.setTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
                engage_txt.setTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
            }else{
//                engage_img_txt.setTextColor(ResourcesCompat.getColor(context.resources, R.color.blackTxt, null))
                engage_txt.setTextColor(ResourcesCompat.getColor(context.resources, R.color.blackTxt, null))
            }

            engage_img_txt.text = engagement.DealerCount.toString()
            engage_txt.text = engagement.DealerCount.toString()
            engage_total_txt.text = totalDealer.toString()

            if(!engagement.EngagementStatusIMgUrl.isEmpty()) {
                glide.load("${Constants.BASE_URL}${engagement.EngagementStatusIMgUrl}").into(engage_img)
                engage_img_ctx.visibility = View.VISIBLE
                engage_total_txt.visibility = View.VISIBLE
                engage_img.visibility = View.VISIBLE
//                engage_view.visibility = View.GONE
                engage_txt.visibility = View.GONE
            }else{
                engage_img_ctx.visibility = View.GONE
                engage_total_txt.visibility = View.GONE
                engage_img.visibility = View.GONE
//                engage_view.visibility = View.GONE
                engage_txt.visibility = View.VISIBLE
            }

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(engagement)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return engagementStatus.size
    }

}