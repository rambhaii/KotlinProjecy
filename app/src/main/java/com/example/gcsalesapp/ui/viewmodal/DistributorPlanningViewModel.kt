package com.example.gcsalesapp.ui.viewmodal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.repository.DealerRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.repository.Utill
import kotlinx.coroutines.launch

class DistributorPlanningViewModel @ViewModelInject constructor(
    private val prefrenceRepository: PrefrenceRepository,
    private val repository: DealerRepository
) : ViewModel() {

    private val dealerInteractionURL: String =
        Constants.BASE_URL + "WebPages/Vol_Intend.aspx?M=#sales_id&Auth_Key=#Auth_Key"

    private var loadUrl=dealerInteractionURL;

    fun getInteractionUrl(): String {
        return loadUrl
    }


    public fun buildInteractionUrl() {
        viewModelScope.launch {

            loadUrl = dealerInteractionURL

            val sales_id = prefrenceRepository.getMarketingRepData().MarketingRepID.toString()
            val auth_key = prefrenceRepository.getAuthKeyData()
            loadUrl = loadUrl.replace("#sales_id", sales_id)
            loadUrl = loadUrl.replace("#Auth_Key", auth_key!!)

            Utill.print("dealerInteractionURL ====${loadUrl}")
        }
    }
}