package com.example.gcsalesapp.ui.viewmodal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.Constants.Companion.LOCAL_ERROR
import com.example.gcsalesapp.data.request.DealerEngagementStatusRequest
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.repository.DealerRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.repository.Utill
import com.example.gcsalesapp.services.TrackingService
import kotlinx.coroutines.launch

class DealerInteractionViewModel @ViewModelInject constructor(
    private val prefrenceRepository: PrefrenceRepository,
    private val repository: DealerRepository
) : ViewModel() {

    private val dealerInteractionURL: String =
        Constants.BASE_URL + "webpages/DealerVisit.aspx?M=#sales_id&D=#dealer_id&Auth_Key=#Auth_Key&Latitude=#Latitude&Longitude=#Longitude&reference_id=#reference_id"

    private var loadUrl=dealerInteractionURL;


    private val _resp = MutableLiveData<ApiState<Dealer>>()
    val validateResp: LiveData<ApiState<Dealer>>
        get() = _resp


    public fun getInteractionUrl(): String {
        return loadUrl
    }


    public fun buildInteractionUrl(dealerId: Int,reference_id:String) {
        viewModelScope.launch {

            loadUrl = dealerInteractionURL

            Utill.print("loadUrl reference_id ====${reference_id}")
            val sales_id = prefrenceRepository.getSalesAgentData().MarketingRepID.toString()
            val auth_key = prefrenceRepository.getAuthKeyData()
            loadUrl = loadUrl.replace("#sales_id", sales_id)
            loadUrl = loadUrl.replace("#Auth_Key", auth_key!!)
            loadUrl = loadUrl.replace("#dealer_id", dealerId.toString())
            loadUrl = loadUrl.replace("#reference_id", reference_id)
            loadUrl = loadUrl.replace("#Latitude", "${TrackingService.pathPoints.value!!.latitude}")
            loadUrl = loadUrl.replace("#Longitude", "${TrackingService.pathPoints.value!!.longitude}")

            Utill.print("dealerInteractionURL ====${loadUrl}")


        }
    }

    public fun setDealerEstimate(dealerId: Int,estimate:String,pageNo:Int,searchStr:String,pin_code:String) {

        if (estimate.isEmpty()) {
            _resp.value = ApiState.Failure("Please enter estimate consumption!",LOCAL_ERROR)
        }else{
            viewModelScope.launch {

                val dealerEngagementStatusRequest = DealerEngagementStatusRequest(
                    0,
                    estimate.toDouble(),
                    pageNo,
                    pin_code,
                    searchStr,
                    dealerId,
                    prefrenceRepository.getMarketingRepData()
                )

                _resp.value = ApiState.Loading()

                when (val repositoryResponse =
                    repository.setDealerEstimateVolume(dealerEngagementStatusRequest)) {

                    is Response.Error -> _resp.value = ApiState.Failure(repositoryResponse.msg!!,repositoryResponse.code)

                    is Response.Success -> {

                        _resp.value =
                            ApiState.Success(
                                repositoryResponse.data!![0],
                                repositoryResponse.msg!!
                            )

                    }

                }

            }
        }


    }

}