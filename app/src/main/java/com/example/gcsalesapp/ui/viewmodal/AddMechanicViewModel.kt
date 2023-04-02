package com.example.gcsalesapp.ui.viewmodal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.request.DealerEngagementStatusRequest
import com.example.gcsalesapp.data.request.GetMechanicForSalesRepRequest
import com.example.gcsalesapp.data.request.Mechanic
import com.example.gcsalesapp.data.request.SaveDealerAssociatedMechanicDataRequest
import com.example.gcsalesapp.data.response.Dealer
import com.example.gcsalesapp.data.response.GetMechanicForSalesRepResponse
import com.example.gcsalesapp.data.response.GetMechanicVehicleTypeResponse
import com.example.gcsalesapp.data.response.SaveDealerAssociatedMechanicDataResponse
import com.example.gcsalesapp.repository.DealerRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.services.TrackingService
import kotlinx.coroutines.launch

class AddMechanicViewModel @ViewModelInject constructor(
    private val prefrenceRepository: PrefrenceRepository,
    private val repository: DealerRepository
) : ViewModel() {


    private val _resp = MutableLiveData<ApiState<SaveDealerAssociatedMechanicDataResponse>>()
    val validateResp: LiveData<ApiState<SaveDealerAssociatedMechanicDataResponse>>
        get() = _resp

    private val _MechanicTypeResp = MutableLiveData<ApiState<GetMechanicVehicleTypeResponse>>()
    val validateMechanicTypeResp: LiveData<ApiState<GetMechanicVehicleTypeResponse>>
        get() = _MechanicTypeResp

    private val _MechanicListResp = MutableLiveData<ApiState<GetMechanicForSalesRepResponse>>()
    val validateMechanicListResp: LiveData<ApiState<GetMechanicForSalesRepResponse>>
        get() = _MechanicListResp


    public fun sendMechanicList(dealerId: Int, mechanics: List<Mechanic>) {


        viewModelScope.launch {

            val saveDealerAssociatedMechanicDataRequest =
                SaveDealerAssociatedMechanicDataRequest(
                    dealerId,
                    prefrenceRepository.getMarketingRepData(),
                    mechanics
                )

            _resp.value = ApiState.Loading()

            when (val repositoryResponse =
                repository.saveMechanicForDealer(saveDealerAssociatedMechanicDataRequest)) {

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

    public fun getAllMechanicList(DealerID: Int = 0, PageNumber: Int = 0, RowsOfPage: Int = 50) {


        viewModelScope.launch {


            val getMechanicForSalesRepRequest = GetMechanicForSalesRepRequest(
                DealerID,
                prefrenceRepository.getMarketingRepData().MarketingRepID,
                PageNumber,
                RowsOfPage
            )

            _resp.value = ApiState.Loading()

            when (val repositoryResponse =
                repository.getMechanics(getMechanicForSalesRepRequest)) {

                is Response.Error -> _resp.value =
                    ApiState.Failure(repositoryResponse.msg!!, repositoryResponse.code)

                is Response.Success -> {

                    _MechanicListResp.value =
                        ApiState.Success(
                            repositoryResponse.data!!,
                            repositoryResponse.msg!!
                        )

                }

            }

        }


    }

    public fun getMechanicTypeList() {
        viewModelScope.launch {
            _MechanicTypeResp.value = ApiState.Loading()

            when (val repositoryResponse =
                repository.getMechanicTypeList(prefrenceRepository.getMarketingRepData())) {

                is Response.Error -> _resp.value =
                    ApiState.Failure(repositoryResponse.msg!!, repositoryResponse.code)

                is Response.Success -> {

                    _MechanicTypeResp.value =
                        ApiState.Success(
                            repositoryResponse.data!!,
                            repositoryResponse.msg!!
                        )

                }

            }
        }
    }

}