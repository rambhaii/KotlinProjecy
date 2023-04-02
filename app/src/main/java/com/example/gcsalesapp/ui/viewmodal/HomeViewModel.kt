package com.example.gcsalesapp.ui.viewmodal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants.Companion.LOCAL_ERROR
import com.example.gcsalesapp.data.request.CallRequest
import com.example.gcsalesapp.data.response.SalesStaff
import com.example.gcsalesapp.repository.LocationRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel @ViewModelInject constructor(
    private val prefrenceRepository: PrefrenceRepository,
    private val repository: LocationRepository
) : ViewModel() {

    private val _resp = MutableLiveData<SalesStaff>()
    val validateResp: LiveData<SalesStaff>
        get() = _resp

    public val callStatus = MutableLiveData<ApiState<String>>()

    private lateinit var salesStaff: SalesStaff


    public fun getSalesStaff() {
        viewModelScope.launch {
            _resp.value = prefrenceRepository.getSalesAgentData()
        }
    }

    public fun logoutSalesStaff() {
        viewModelScope.launch {
            prefrenceRepository.setLogout()
        }
    }

    public fun callToDealer(phone: String) {
        viewModelScope.launch {
            salesStaff = prefrenceRepository.getSalesAgentData()
            if (salesStaff.CallUserID == null || salesStaff.CallUserID.isEmpty()) {
                callStatus.value = ApiState.Failure("Caller id not Exist!", LOCAL_ERROR)
            } else {
                val refrenceNo = "${Date().time}"
                val phoneNumber = if (phone.startsWith("+91")) phone else "+91" + phone
                when (val response = repository.callToDealer(
                    CallRequest(
                        "",
                        phoneNumber,
                        "",
                        refrenceNo,
                        "",
                        "",
                        salesStaff.CallUserID
                    )
                )) {
                    is Response.Error -> callStatus.value =
                        ApiState.Failure(response.msg!!, response.code)

                    is Response.Success -> {
                        callStatus.value = ApiState.Success(refrenceNo, response.data!!.msg)

                    }
                }
            }
        }
    }

}