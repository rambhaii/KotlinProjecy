package com.example.gcsalesapp.ui.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.adapters.DealerPaggingAdapter
import com.example.gcsalesapp.adapters.EngagementStatusAdapter
import com.example.gcsalesapp.adapters.State
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.repository.Utill
import com.example.gcsalesapp.ui.view.dialogs.CustomMessageDialog
import com.example.gcsalesapp.ui.view.dialogs.CustomProgressDialog
import com.example.gcsalesapp.ui.viewmodal.ApiState
import com.example.gcsalesapp.ui.viewmodal.MonitorDealerViewModel
import com.example.gcsalesapp.ui.viewmodal.TrackingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitor_dealer.*
import kotlinx.android.synthetic.main.view_sales_info.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitorDealerActivity : BaseActivity() {

    @Inject
    lateinit var engagementStatusAdapter: EngagementStatusAdapter

    @Inject
    lateinit var dealerAdapter: DealerPaggingAdapter

    private val viewModel: MonitorDealerViewModel by viewModels()
    private val trackingViewModel: TrackingViewModel by viewModels()
    private val progressDialog = CustomProgressDialog()
    private val messageDialog = CustomMessageDialog()

    private var engagementId = 0
    private var pageNO = 1

    private var isLocation = false
    private var isSearch = false
    private var pinCode = ""

    private var dealers: ArrayList<Dealer> = arrayListOf()
//    private var matchedDealers: ArrayList<Dealer> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor_dealer)

        sales_info_ctx.visibility = View.GONE
        search_ctx.visibility = View.VISIBLE

//        engagementId.value = 0

        setActiveColor()


        location_img.setOnClickListener {
            isLocation = !isLocation
            setActiveColor()
        }


        refresh_btn.setOnClickListener {
//            initPage()
            engagementId=0
            search_txt.setText("")
            setSearchAction()
            isLocation = false
            setActiveColor()
        }


//        engagementId.observe(this, {
//            viewModel.getDealerEngagementList(it)
//        })


        trackingViewModel.validateAddressResp.observe(this, { state ->
            when (state) {
                is ApiState.Success -> {
                    location_txt.text = state.body
                    Utill.print("validateAddressResp == ${state.message}")
                    pinCode = state.message
                }
            }
        })

        search_img.setOnClickListener {
//            isSearch=!isSearch
            if (isSearch) {
                search_txt.setText("")
                getDealerEngagement()
            }

            setSearchAction()

            if (search_txt.text.length > 0) {
                getDealerEngagement()
            }

        }


//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                search(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                search(newText)
//                return true
//            }
//        })

//        swipe_refresh.setOnRefreshListener {
//            viewModel.getDealerEngagementList(0)
//        }


        engagementRecyclerView.apply {
            adapter = engagementStatusAdapter
            layoutManager = LinearLayoutManager(
                this@MonitorDealerActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        engagementStatusAdapter.setOnItemClickListener {
            engagementId = it.EngagementStatusID
//            isLocation=false
//            pinCode=""
//            search_txt.setText("")
//            setActiveColor()
            initPage()
            getDealerEngagement()
        }

        dealerRecyclerView.apply {
            adapter = dealerAdapter
            layoutManager = LinearLayoutManager(
                this@MonitorDealerActivity
            )
        }


        var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
//                    isLocation=false
//                    pinCode=""
//                    search_txt.setText("")
//                    setActiveColor()
                    getDealerEngagement()
                }
            }


        dealerAdapter.setOnInteractionClickListener { dealer ->
            Intent(this, DealerInteractionActivity::class.java).also {
                it.putExtra("Dealer", dealer)
                resultLauncher.launch(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )

            }

        }

        dealerAdapter.setOnLoadMoreListener {
            Utill.print("====on load more..${pageNO++}")
            getDealerEngagement()
        }



        dealerAdapter.setOnOrderClickListener { dealer ->
            Intent(this, DealerOrdersActivity::class.java).also {
                it.putExtra("Dealer", dealer)
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }

        }

        dealerAdapter.setOnItemClickListener { dealer ->
            Intent(this, AddMechanicActivity::class.java).also {
                it.putExtra("Dealer", dealer)
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }
        }

        viewModel.validateResp.observe(this, { state ->
            when (state) {

                is ApiState.Loading -> {
                    progressDialog.show(this, "Please Wait...")
                }
                is ApiState.Failure -> {
//                    swipe_refresh.isRefreshing=false
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }
                    messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
                }
                is ApiState.Success -> {
//                    swipe_refresh.isRefreshing=false
                    if (progressDialog.isShowing) {
                        progressDialog.dialog.dismiss()
                    }

                    if (engagementId == 0) {
                        engagementStatusAdapter.engagementStatus = state.body.Summary
                        engagementStatusAdapter.totalDealer = state.body.TotalDealerCount
                    }

                    if (state.body.Dealers.size > 0) {
                        dealers.addAll(state.body.Dealers as ArrayList<Dealer>)
                        Utill.print("dealers === ${dealers.size}")
//                    dealerAdapter.setState()
                        dealerAdapter.engagementStatus = dealers
                        dealerAdapter.setState(State.LOADING)
                    } else {
//                        pageNO--
                        dealerAdapter.setState(State.DONE)
                    }
//                    dealerAdapter.notifyDataSetChanged()

//                    messageDialog.show(this, CustomMessageDialog.MessageType.SUCCESS, "${state.body}")
                }
            }
        })

    }

    private fun setActiveColor() {
        initPage()
        if (!isLocation) {
            location_img.setImageResource(R.drawable.ic_baseline_location_searching_24)
            val starColor =
                Color.parseColor("#969292")
//            pinCode = ""
            location_img.setColorFilter(starColor)
        } else {
            location_img.setImageResource(R.drawable.ic_baseline_my_location_24)

            val starColor =
                Color.parseColor("#FFb27700")

            location_img.setColorFilter(starColor)

//            Utill.print("pinCode == ${pinCode}")

        }
        getDealerEngagement()
    }


//    private fun search(text: String?) {
//        matchedDealers = arrayListOf()
//
//        text?.let {
//            dealers.forEach { dealer ->
//                if (dealer.DealershipName.contains(text, true) ||
//                    dealer.PhoneNo.contains(text, true) ||
//                    dealer.PINCode.contains(text, true)
//                ) {
//                    matchedDealers.add(dealer)
//                }
//            }
//
//            dealerAdapter.engagementStatus = matchedDealers
//            dealerAdapter.notifyDataSetChanged()
//
////            updateRecyclerView()
////            if (matchedDealers.isEmpty()) {
////                Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show()
////            }
////            updateRecyclerView()
//        }
//    }

    private fun setSearchAction() {
        initPage()
        if (search_txt.text.length > 0) {
            isSearch = true
            search_img.setImageResource(R.drawable.ic_outline_clear_24)
        } else {
            isSearch = false
            search_img.setImageResource(R.drawable.ic_baseline_search_24)
        }
    }

    private fun initPage() {
        pageNO = 1
        dealers.clear()
        dealerAdapter.engagementStatus=dealers
        dealerAdapter.setState(State.LOADING)

    }

    private fun getDealerEngagement() {
        Utill.print("pinCode == ${pinCode}")
//        if(!isLocation){
//            pinCode=""
//        }

        if (isLocation) {
            viewModel.getDealerEngagementList(
                engagementStatusID = engagementId,
                pinCode = pinCode,
                pageNo = pageNO,
                searchTxt = search_txt.text.toString().trim()
            )
        } else {
            viewModel.getDealerEngagementList(
                engagementStatusID = engagementId,
                pageNo = pageNO,
                searchTxt = search_txt.text.toString().trim()
            )
        }
    }

}