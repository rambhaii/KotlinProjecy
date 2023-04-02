package com.example.gcsalesapp.ui.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gcsalesapp.R
import com.example.gcsalesapp.adapters.DealerOrderAdapter
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.ui.view.dialogs.CustomMessageDialog
import com.example.gcsalesapp.ui.view.dialogs.CustomProgressDialog
import com.example.gcsalesapp.ui.viewmodal.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dealer_interaction.*
import kotlinx.android.synthetic.main.activity_dealer_orders.*
import kotlinx.android.synthetic.main.activity_monitor_dealer.*
import kotlinx.android.synthetic.main.view_dealer_header.*
import kotlinx.android.synthetic.main.view_dealer_list_item.view.*
import kotlinx.android.synthetic.main.view_sales_info.*
import javax.inject.Inject

@AndroidEntryPoint
class DealerOrdersActivity : BaseActivity() {

    private val viewModel: DealerOrdersViewModel by viewModels()
//    private val homeViewModel: HomeViewModel by viewModels()
    private val trackingViewModel: TrackingViewModel by viewModels()
    private var dealer: Dealer? = null
    private val progressDialog = CustomProgressDialog()
    private val messageDialog = CustomMessageDialog()

    @Inject
    lateinit var dealerOrderAdapter: DealerOrderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer_orders)

        sales_info_ctx.visibility= View.GONE

        dealer = intent.getParcelableExtra<Dealer>("Dealer")

        viewModel.getDealerOrderList(dealer!!.DealerID.toInt())

        dealer_name.text = dealer!!.DealershipName
        dealer_type.text = dealer!!.DealershipType
        dealer_avg.text =
            if (dealer!!.CalculatedVolPerMonth > 0) "(${dealer!!.CalculatedVolPerMonth} Lit.)" else "(${dealer!!.EstimatedVolPerMonth} Lit.)"


        dealer_head_card.visibility = INVISIBLE

        trackingViewModel.validateAddressResp.observe(this, { state ->
            when (state) {
                is ApiState.Success -> {
                    location_txt.text = state.body
                }
            }
        })

//        homeViewModel.callStatus.observe(this, { state ->
//            when (state) {
//                is ApiState.Failure -> {
//                    call_dealer.setImageResource(R.drawable.ic_baseline_call_24)
//                    messageDialog.show(this,CustomMessageDialog.MessageType.ERROR,state.errorText)
//                }
//                is ApiState.Success -> {
//                    call_dealer.setImageResource(R.drawable.ic_baseline_phone_in_talk_24)
//                    calling_order_img.visibility = View.VISIBLE
//                    Glide.with(this)
//                        .load(R.drawable.calling)
//                        .into(calling_order_img);
//
//                    val timer = object: CountDownTimer(5000, 1000) {
//                        override fun onTick(millisUntilFinished: Long) {
//
//                        }
//
//                        override fun onFinish() {
//                            calling_order_img.visibility = View.GONE
//                        }
//                    }
//                    timer.start()
//                }
//            }
//        })


        call_dealer.setOnClickListener {
//            homeViewModel.callToDealer(dealer!!.PhoneNo)
        }


        orderRecyclerView.apply {
            adapter = dealerOrderAdapter
            layoutManager = LinearLayoutManager(this@DealerOrdersActivity)
        }


        viewModel.validateResp.observe(this, { state ->
            when (state) {

                is ApiState.Loading -> {
                    progressDialog.show(this, "Please Wait...")
                }
                is ApiState.Failure -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
                }
                is ApiState.Success -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }

                    dealerOrderAdapter.engagementStatus = state.body

//                    messageDialog.show(
//                        this,
//                        CustomMessageDialog.MessageType.SUCCESS,
//                        "${state.body}"
//                    )
                }
            }
        })

    }
}