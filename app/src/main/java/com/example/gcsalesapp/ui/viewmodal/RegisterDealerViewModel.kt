package com.example.gcsalesapp.ui.viewmodal

import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.Constants.Companion.LOCAL_ERROR
import com.example.gcsalesapp.data.request.DealerEngagementStatusRequest
import com.example.gcsalesapp.data.request.Mechanic
import com.example.gcsalesapp.data.request.RegisterNewDealerBySalesStaff
import com.example.gcsalesapp.data.request.SaveDealerAssociatedMechanicDataRequest
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.data.response.RegisterDealerResponse
import com.example.gcsalesapp.data.response.SaveDealerAssociatedMechanicDataResponse
import com.example.gcsalesapp.repository.DealerRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.services.TrackingService
import kotlinx.coroutines.launch

class RegisterDealerViewModel @ViewModelInject constructor(
    private val prefrenceRepository: PrefrenceRepository,
    private val repository: DealerRepository
) : ViewModel() {


    private val _resp = MutableLiveData<ApiState<RegisterDealerResponse>>()
    val validateResp: LiveData<ApiState<RegisterDealerResponse>>
        get() = _resp


    public fun registerDealer(data: Bundle) {

        if (data.getString("DealershipName", "").isEmpty()) {
            _resp.value = ApiState.Failure("Please enter dealership name!",LOCAL_ERROR)
        }else if (data.getString("Name", "").isEmpty()) {
            _resp.value = ApiState.Failure("Please enter name!",LOCAL_ERROR)
        }else if (data.getString("Surname", "").isEmpty()) {
            _resp.value = ApiState.Failure("Please enter surname!",LOCAL_ERROR)
        }else if (data.getString("PhoneNo", "").isEmpty()) {
            _resp.value = ApiState.Failure("Please enter phone number!",LOCAL_ERROR)
        }else if (data.getString("PhoneNo", "").length!=10) {
            _resp.value = ApiState.Failure("Please enter 10 digit phone number!",LOCAL_ERROR)
        }else if (data.getString("PinCode", "").isEmpty()) {
            _resp.value = ApiState.Failure("Please enter pin code!",LOCAL_ERROR)
        }else if (data.getString("PinCode", "").length!=6) {
            _resp.value = ApiState.Failure("Please enter 6 digit pin code!",LOCAL_ERROR)
        }else {
            viewModelScope.launch {

                val registerNewDealerBySalesStaff =
                    RegisterNewDealerBySalesStaff(
                        prefrenceRepository.getMarketingRepData(),
                        data.getString("DealershipName", ""),
                        data.getString("MiddleName", ""),
                        data.getString("Name", ""),
                        data.getString("PhoneNo", ""),
                        data.getString("PinCode", ""),
                        data.getString("Surname", ""),
                    )

                _resp.value = ApiState.Loading()

                when (val repositoryResponse =
                    repository.registerDealer(registerNewDealerBySalesStaff)) {

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

}