package com.example.gcsalesapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.adapters.MechanicAdapter
import com.example.gcsalesapp.adapters.MechanicPaggingAdapter
import com.example.gcsalesapp.adapters.State
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.data.response.MechanicData
import com.example.gcsalesapp.repository.Utill
import com.example.gcsalesapp.ui.view.dialogs.CustomMessageDialog
import com.example.gcsalesapp.ui.view.dialogs.CustomProgressDialog
import com.example.gcsalesapp.ui.viewmodal.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_mechanic.*
import kotlinx.android.synthetic.main.activity_add_mechanic.mechanic_recyclerview
import kotlinx.android.synthetic.main.activity_mechanic_list.*
import kotlinx.android.synthetic.main.view_dealer_header.*
import kotlinx.android.synthetic.main.view_dealer_list_item.*
import kotlinx.android.synthetic.main.view_dealer_list_item.dealer_avg
import kotlinx.android.synthetic.main.view_dealer_list_item.dealer_name
import kotlinx.android.synthetic.main.view_dealer_list_item.dealer_type
import kotlinx.android.synthetic.main.view_sales_info.*
import javax.inject.Inject

@AndroidEntryPoint
class MechanicListActivity : BaseActivity() {

    private val addMechanicViewModel: AddMechanicViewModel by viewModels()
    private val trackingViewModel: TrackingViewModel by viewModels()
    private val viewModel: HomeViewModel by viewModels()

    private val progressDialog = CustomProgressDialog()
    private val messageDialog = CustomMessageDialog()

    private var mechanics: ArrayList<MechanicData> = arrayListOf()

    private var dealer: Dealer? = null

    private var dealerId=0
    private var pageNO = 1

    @Inject
    lateinit var mechanicAdapter: MechanicPaggingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanic_list)

        sales_info_ctx.visibility = View.GONE


        trackingViewModel.validateAddressResp.observe(this, { state ->
            when (state) {
                is ApiState.Success -> {
                    location_txt.text = state.body
                }
            }
        })


        viewModel.getSalesStaff()

        viewModel.validateResp.observe(this,{
            if(!intent.hasExtra("Dealer")) {
                dealer_name.text = "${it.MarketingRepFName} ${it.MarketingRepLName}"
            }
        })



        dealer_avg.visibility = GONE
        call_dealer.visibility=GONE
        dealer_head_card.visibility= View.INVISIBLE

        if(intent.hasExtra("Dealer")) {

            dealer = intent.getParcelableExtra<Dealer>("Dealer")

            Utill.print("dealer == ${dealer.toString()}")

            dealer_name.text = dealer!!.DealershipName
//            dealer_type.text = dealer!!.DealershipType

            dealerId = dealer!!.DealerID.toInt()
        }

        addMechanicViewModel.getAllMechanicList(DealerID = dealerId,PageNumber = pageNO)

        mechanic_recyclerview.apply {
            adapter = mechanicAdapter
            layoutManager = LinearLayoutManager(this@MechanicListActivity)
        }


        mechanicAdapter.setOnLoadMoreListener {
            pageNO++
          addMechanicViewModel.getAllMechanicList(DealerID = dealerId,PageNumber = pageNO)
        }


        addMechanicViewModel.validateMechanicListResp.observe(this, { state ->
            when (state) {

                is ApiState.Loading -> {
                    if (!progressDialog.isShowing) {
                        progressDialog.show(this, "Please Wait...")
                    }
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
                    dealer_type.text = "Mechanics : ${state.body.RecordCount[0].MechanicCount}"

                    if (state.body.Data.size > 0) {
                        mechanics.addAll(state.body.Data as ArrayList<MechanicData>)
                        Utill.print("mechanics === ${mechanics.size}")
                        mechanicAdapter.engagementStatus = mechanics
                        mechanicAdapter.setState(State.LOADING)
                    } else {
//                        pageNO--
                        mechanicAdapter.setState(State.DONE)
                    }


//                    messageDialog.show(this, CustomMessageDialog.MessageType.SUCCESS, state.message)
                }

            }
        })

    }

}