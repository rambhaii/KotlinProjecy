package com.example.gcsalesapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gcsalesapp.R
import com.example.gcsalesapp.api.GCWebViewClient
import com.example.gcsalesapp.ui.viewmodal.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dealer_interaction.*
import kotlinx.android.synthetic.main.view_sales_info.*

@AndroidEntryPoint
class DistrutedPlanningActivity : BaseActivity() {

    private val distributorPlanningViewModel: DistributorPlanningViewModel by viewModels()
    private val trackingViewModel: TrackingViewModel by viewModels()

    private var isChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distruted_planning)

        sales_info_ctx.visibility = View.GONE
        relativeLayout.visibility = View.GONE

        distributorPlanningViewModel.buildInteractionUrl();

        val gcWebViewClient = GCWebViewClient()
        interactionWebView.webViewClient = gcWebViewClient
       interactionWebView.loadUrl(distributorPlanningViewModel.getInteractionUrl())
        interactionWebView.settings.javaScriptEnabled = true
        interactionWebView.settings.setSupportZoom(true)

        gcWebViewClient.validateResp.observe(this, {
            if (it.equals("Success", true)) {
                setResult(RESULT_OK, Intent())
                finish()
            }
        })

        trackingViewModel.validateAddressResp.observe(this, { state ->
            when (state) {
                is ApiState.Success -> {
                    location_txt.text = state.body
                }
            }
        })

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