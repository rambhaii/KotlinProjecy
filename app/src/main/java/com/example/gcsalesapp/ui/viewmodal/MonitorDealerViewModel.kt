package com.example.gcsalesapp.ui.viewmodal

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.request.DealerEngagementStatusRequest
import com.example.gcsalesapp.data.request.ValidateSalesPersonRequest
import com.example.gcsalesapp.data.response.DealerEngagementStatusResponse
import com.example.gcsalesapp.data.response.SalesStaff
import com.example.gcsalesapp.repository.DealerRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import kotlinx.coroutines.launch

class MonitorDealerViewModel @ViewModelInject constructor(
    private val repository: DealerRepository,
    private val preference: PrefrenceRepository
) : ViewModel() {

    private val _resp = MutableLiveData<ApiState<DealerEngagementStatusResponse>>()
    val validateResp: LiveData<ApiState<DealerEngagementStatusResponse>>
        get() = _resp


    public fun getDealerEngagementList(engagementStatusID: Int=0,pageNo:Int=0,pinCode:String="",searchTxt:String="") {

        viewModelScope.launch {

            val dealerEngagementStatusRequest = DealerEngagementStatusRequest(
                engagementStatusID,
                0.0,
                pageNo,
                pinCode,
                searchTxt,
                0,
                preference.getMarketingRepData()
            )

            _resp.value = ApiState.Loading()

            when (val loginResponse =
                repository.getDealerListByEngagementID(dealerEngagementStatusRequest)) {

                is Response.Error -> _resp.value = ApiState.Failure(loginResponse.msg!!,loginResponse.code)

                is Response.Success -> {

                    val respData = loginResponse.data!! as DealerEngagementStatusResponse
                    _resp.value =
                        ApiState.Success(respData, loginResponse.msg!!)

                }

            }

        }
    }

}