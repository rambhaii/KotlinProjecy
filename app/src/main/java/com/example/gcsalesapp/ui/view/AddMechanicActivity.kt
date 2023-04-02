package com.example.gcsalesapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gcsalesapp.R
import com.example.gcsalesapp.adapters.MechanicAdapter
import com.example.gcsalesapp.data.request.Mechanic
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.data.response.MechanicType
import com.example.gcsalesapp.repository.Utill
import com.example.gcsalesapp.ui.view.dialogs.AddMechanicBottomSheetDialog
import com.example.gcsalesapp.ui.view.dialogs.CallBackListener
import com.example.gcsalesapp.ui.view.dialogs.CustomMessageDialog
import com.example.gcsalesapp.ui.view.dialogs.CustomProgressDialog
import com.example.gcsalesapp.ui.viewmodal.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_mechanic.*
import kotlinx.android.synthetic.main.view_dealer_header.*
import kotlinx.android.synthetic.main.view_sales_info.*
import javax.inject.Inject

@AndroidEntryPoint
class AddMechanicActivity : BaseActivity() {

    private var dealer: Dealer? = null
    private val addMechanicViewModel: AddMechanicViewModel by viewModels()
    private val trackingViewModel: TrackingViewModel by viewModels()
    private val mechanicList = ArrayList<Mechanic>()
    private val mechanicTypeList = ArrayList<MechanicType>()
    private val mechanicLiveData = MutableLiveData<ArrayList<Mechanic>>()

    private val progressDialog = CustomProgressDialog()
    private val messageDialog = CustomMessageDialog()

    @Inject
    lateinit var mechanicAdapter: MechanicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mechanic)
        sales_info_ctx.visibility = View.GONE

        mechanicLiveData.value = mechanicList

        dealer = intent.getParcelableExtra<Dealer>("Dealer")

        Utill.print("dealer == ${dealer.toString()}")

        dealer_name.text = dealer!!.DealershipName
        dealer_type.text = dealer!!.DealershipType
//        dealer_avg.text =
//            if (dealer!!.CalculatedVolPerMonth > 0) "(${dealer!!.CalculatedVolPerMonth} Lit.)" else "(${dealer!!.EstimatedVolPerMonth} Lit.)"


        addMechanicViewModel.getAllMechanicList(DealerID = dealer!!.DealerID.toInt())

        call_dealer.setImageResource(R.drawable.ic_baseline_group_24)
        call_dealer.setOnClickListener {
            Intent(this, MechanicListActivity::class.java).also {
                it.putExtra("Dealer", dealer)
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }
        }


        mechanic_recyclerview.apply {
            mechanicAdapter.engagementStatus = mechanicList
            adapter = mechanicAdapter
            layoutManager = LinearLayoutManager(this@AddMechanicActivity)
        }


        mechanicAdapter.setOnDeleteClickListener { mechanic ->
            mechanicList.remove(mechanic)
            mechanicLiveData.value = mechanicList
            mechanicAdapter.engagementStatus = mechanicList
            mechanicAdapter.notifyDataSetChanged()
        }


        trackingViewModel.validateAddressResp.observe(this, { state ->
            when (state) {
                is ApiState.Success -> {
                    location_txt.text = state.body
                }
            }
        })


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
                    dealer_avg.text = "${state.body.RecordCount[0].MechanicCount}"


//                    messageDialog.show(this, CustomMessageDialog.MessageType.SUCCESS, state.message)
                }

            }
        })


//        homeViewModel.callStatus.observe(this, { state ->
//            when (state) {
//                is ApiState.Failure -> {
//                    call_dealer.setImageResource(R.drawable.ic_baseline_call_24)
//                    messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, state.errorText)
//                }
//                is ApiState.Success -> {
//                    call_dealer.setImageResource(R.drawable.ic_baseline_phone_in_talk_24)
//                    calling_mech_img.visibility = View.VISIBLE
//                    Glide.with(this)
//                        .load(R.drawable.calling)
//                        .into(calling_mech_img);
//
//                    val timer = object : CountDownTimer(5000, 1000) {
//                        override fun onTick(millisUntilFinished: Long) {
//
//                        }
//
//                        override fun onFinish() {
//                            calling_mech_img.visibility = View.GONE
//                        }
//                    }
//                    timer.start()
//                }
//            }
//        })

        mechanicLiveData.observe(this, { mechanics ->

            Utill.print("mechanicLiveData size == ${mechanics.size}")

            if (mechanics.size > 0) {
                submit_mechanic_btn.visibility = View.VISIBLE
            } else {
                submit_mechanic_btn.visibility = View.GONE
            }
        })

        submit_mechanic_btn.setOnClickListener {
            addMechanicViewModel.sendMechanicList(dealer!!.DealerID.toInt(), mechanicList)
        }

        addMechanicViewModel.getMechanicTypeList()

        addMechanicViewModel.validateMechanicTypeResp.observe(this, { state ->
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

                    mechanicTypeList.addAll(state.body as ArrayList<MechanicType>)
                    openSheet()

//                    messageDialog.show(this, CustomMessageDialog.MessageType.SUCCESS, state.message)
                }

            }
        })


        add_mechanic_action_btn.setOnClickListener {
            openSheet()
        }


        addMechanicViewModel.validateResp.observe(this, {

                state ->
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
                    submit_mechanic_btn.visibility = View.GONE
                    mechanicList.clear()
                    messageDialog.show(this, CustomMessageDialog.MessageType.SUCCESS, state.message)
                        .setOnDismissListener {
                            finish()
                        }
                }


            }

        })


    }

    private fun openSheet() {
        AddMechanicBottomSheetDialog(
            mechanicTypeList,
            object : CallBackListener {
                override fun onCallBack(data: Bundle) {
                    addMechanic(data)
                    mechanicLiveData.value = mechanicList
                    mechanicAdapter.engagementStatus = mechanicList
                    mechanicAdapter.notifyDataSetChanged()
                }
            }).show(
            supportFragmentManager,
            "AddMechanicSheet"
        )
    }

    private fun addMechanic(data: Bundle) {
        if (data.getString("first_name")!!.isEmpty()) {
            messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, "Enter mechanic name.")
        } else if (data.getString("first_name")!!.length < 3) {
            messageDialog.show(
                this,
                CustomMessageDialog.MessageType.ERROR,
                "Enter valid mechanic name."
            )
        } else if (data.getString("phone")!!.isEmpty()) {
            messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, "Enter phone number.")
        } else if (data.getString("phone")!!.length != 10) {
            messageDialog.show(
                this,
                CustomMessageDialog.MessageType.ERROR,
                "Enter valid phone number."
            )
        } else if (data.getString("pin")!!.length > 0 && data.getString("pin")!!.length != 6) {
            messageDialog.show(
                this,
                CustomMessageDialog.MessageType.ERROR,
                "Enter valid pin code."
            )
        } else if (data.getInt("MechanicVehicleTypeID") == 0) {
            messageDialog.show(this, CustomMessageDialog.MessageType.ERROR, "Select mechanic type")
        } else {
            mechanicList.add(
                Mechanic(
                    data.getString("first_name")!!,
                    data.getString("last_name")!!,
                    data.getString("phone")!!,
                    data.getString("MechanicVehicleTypeName")!!,
                    data.getInt("MechanicVehicleTypeID")!!,
                    data.getString("pin")!!
                )
            )
        }
    }
}