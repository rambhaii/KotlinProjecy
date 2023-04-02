package com.example.gcsalesapp.ui.viewmodal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.request.DealerEngagementStatusRequest
import com.example.gcsalesapp.data.response.DealerEngagementStatusResponse
import com.example.gcsalesapp.data.response.DealerOrderListResponse
import com.example.gcsalesapp.repository.DealerRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import kotlinx.coroutines.launch

class DealerOrdersViewModel @ViewModelInject constructor(
    private val preference: PrefrenceRepository,
    private val repository: DealerRepository
) : ViewModel() {

    private val _resp = MutableLiveData<ApiState<DealerOrderListResponse>>()
    val validateResp: LiveData<ApiState<DealerOrderListResponse>>
        get() = _resp

    public fun getDealerOrderList(dealerId: Int) {

        viewModelScope.launch {

            val dealerEngagementStatusRequest = DealerEngagementStatusRequest(
                0,
                0.0,
                0,
                "",
                "",
                dealerId,
                preference.getMarketingRepData()
            )

            _resp.value = ApiState.Loading()

            when (val repositoryResponse =
                repository.geOrderListByDealerID(dealerEngagementStatusRequest)) {

                is Response.Error -> _resp.value =
                    ApiState.Failure(repositoryResponse.msg!!, repositoryResponse.code)

                is Response.Success -> {

                    _resp.value =
                        ApiState.Success(
                            repositoryResponse.data!!,
                            repositoryResponse.msg!!
                        )

                }

            }

        }
    }

}