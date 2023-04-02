package com.example.gcsalesapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.gcsalesapp.R
import com.example.gcsalesapp.api.GCWebViewClient
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.repository.Utill
import com.example.gcsalesapp.ui.view.dialogs.*
import com.example.gcsalesapp.ui.viewmodal.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dealer_interaction.*
import kotlinx.android.synthetic.main.view_dealer_header.*
import kotlinx.android.synthetic.main.view_dealer_list_item.dealer_avg
import kotlinx.android.synthetic.main.view_dealer_list_item.dealer_name
import kotlinx.android.synthetic.main.view_dealer_list_item.dealer_type
import kotlinx.android.synthetic.main.view_sales_info.*

@AndroidEntryPoint
class DealerInteractionActivity : BaseActivity() {

    private val viewModel: DealerInteractionViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val trackingViewModel: TrackingViewModel by viewModels()
    private var dealer: Dealer? = null

    //    private lateinit var bottomSheet: EstimateBottomSheetDialog
    private val progressDialog = CustomProgressDialog()
    private val messageDialog = CustomMessageDialog()

    private var pageNo = 1
    private var isChange = false

//    @Inject
//    private lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer_interaction)


        sales_info_ctx.visibility = View.GONE

//        viewModel.buildInteractionUrl(intent.getIntExtra("DealerID",0));

        dealer = intent.getParcelableExtra<Dealer>("Dealer")

        viewModel.buildInteractionUrl(dealer?.DealerID!!.toInt(),"");



//        glide.load(R.drawable.calling)
//            .into(no_engage_img)

        val gcWebViewClient = GCWebViewClient()
        interactionWebView.webViewClient = gcWebViewClient
        interactionWebView.loadUrl(viewModel.getInteractionUrl())
        interactionWebView.settings.javaScriptEnabled = true
        interactionWebView.settings.setSupportZoom(true)

        gcWebViewClient.validateResp.observe(this, {
            if (it.equals("Success", true)) {
                setResult(RESULT_OK, Intent())
                finish()
            }
        })




        Utill.print("dealer == ${dealer.toString()}")

        dealer_name.text = dealer?.DealershipName
        dealer_type.text = dealer?.DealershipType
        dealer_avg.text =
            if (dealer!!.CalculatedVolPerMonth > 0) "(${dealer!!.CalculatedVolPerMonth} Lit.)" else "(${dealer!!.EstimatedVolPerMonth} Lit.)"

        if (dealer?.CalculatedVolPerMonth == 0.0 && dealer?.EstimatedVolPerMonth == 0.0) {

            val bottomSheet = EstimateBottomSheetDialog(object : CallBackListener {
                override fun onCallBack(data: Bundle) {
                    isChange = true
                    viewModel.setDealerEstimate(
                        dealer!!.DealerID.toInt(),
                        data.getString("estimate", "0"),
                        pageNo,
                        "",
                        ""
                    )
                }
            })
            bottomSheet.isCancelable = false
            bottomSheet.show(
                supportFragmentManager,
                "ModalBottomSheet"
            )
        }


        viewModel.validateResp.observe(this, { state ->
            when (state) {

                is ApiState.Loading -> {
                    progressDialog.show(this, "Please Wait...")
                }
                is ApiState.Success -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    dealer_avg.text =
                        "(${state.body.EstimatedVolPerMonth} Lit.)"

                }
                is ApiState.Failure -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
                }
            }
        })

        homeViewModel.callStatus.observe(this, { state ->
            when (state) {
                is ApiState.Failure -> {
                    call_dealer.setImageResource(R.drawable.ic_baseline_call_24)
                    messageDialog.show(this,CustomMessageDialog.MessageType.ERROR,state.errorText)
                }
                is ApiState.Success -> {
                    Utill.print("callStatus Success ====${state.body}")
                    viewModel.buildInteractionUrl(dealer!!.DealerID.toInt(),state.body);
                    call_dealer.setImageResource(R.drawable.ic_baseline_phone_in_talk_24)
                    calling_img.visibility = View.VISIBLE
                    Glide.with(this)
                        .load(R.drawable.calling)
                        .into(calling_img);

                    val timer = object: CountDownTimer(5000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {

                        }

                        override fun onFinish() {
                            interactionWebView.loadUrl(viewModel.getInteractionUrl())
                            calling_img.visibility = View.GONE
                        }
                    }
                    timer.start()

                }
            }
        })

        trackingViewModel.validateAddressResp.observe(this, { state ->
            when (state) {
                is ApiState.Success -> {
                    location_txt.text = state.body
                }
            }
        })

        call_dealer.setOnClickListener {
            homeViewModel.callToDealer(dealer!!.PhoneNo)
        }

    }

    override fun onBackPressed() {
        if (isChange) {
            setResult(RESULT_OK, Intent())
            finish()
        } else {
            super.onBackPressed()
        }
    }


}