package com.example.gcsalesapp.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.gcsalesapp.R
import com.example.gcsalesapp.ui.viewmodal.ApiState
import com.example.gcsalesapp.ui.viewmodal.HomeViewModel
import com.example.gcsalesapp.ui.viewmodal.TrackingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_sales_info.*

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private val trackingViewModel: TrackingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel.getSalesStaff()

        viewModel.validateResp.observe(this,{
            name_txt.text = "${it.MarketingRepFName} ${it.MarketingRepLName}"
        })


        trackingViewModel.validateAddressResp.observe(this, { state ->
            when (state) {
                is ApiState.Success -> {
                    location_txt.text = state.body
                }
            }
        })


        register_card.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }
        }

        monitor_card.setOnClickListener {
            Intent(this, MonitorDealerActivity::class.java).also {
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }

        }

        mechanic_card.setOnClickListener {
            Intent(this, MechanicListActivity::class.java).also {
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }
        }

        distributed_card.setOnClickListener {
            Intent(this, DistrutedPlanningActivity::class.java).also {
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }
        }

        user_img.setOnClickListener {
            viewModel.logoutSalesStaff()
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                finish()
            }

        }

    }


}