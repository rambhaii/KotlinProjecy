package com.example.gcsalesapp.ui.viewmodal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.request.ValidateSalesPersonRequest
import com.example.gcsalesapp.data.response.SalesStaff
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.repository.ValidateRepository
import kotlinx.coroutines.launch

class LoginViewModal @ViewModelInject constructor(
    private val repository: ValidateRepository,
    private val prefrenceRepository: PrefrenceRepository
) : ViewModel() {


    sealed class ValidateState {
        class Success(
            val salesStaff: SalesStaff,
            val message: String
        ) :
            ValidateState()

        class Failure(val errorText: String) : ValidateState()
        object Loading : ValidateState()
        object Empty : ValidateState()
    }


    private val _resp = MutableLiveData<ValidateState>()
    val validateResp: LiveData<ValidateState>
        get() = _resp


    public fun loginSellsPerson(validateSalesPersonRequest: ValidateSalesPersonRequest) {


        if (validateSalesPersonRequest.PhoneNo.isEmpty()) {
            _resp.value = ValidateState.Failure("Please enter phone number!")

        } else if (validateSalesPersonRequest.OTP.isEmpty()) {
            _resp.value = ValidateState.Failure("Please enter today's password!")

        } else if (validateSalesPersonRequest.PhoneNo.length != 10) {
            _resp.value = ValidateState.Failure("Please enter valid phone number!")

        } else {
            viewModelScope.launch {

                _resp.value = ValidateState.Loading

                when (val loginResponse =
                    repository.validateSalesLogin(validateSalesPersonRequest)) {

                    is Response.Error -> _resp.value = ValidateState.Failure(loginResponse.msg!!)

                    is Response.Success -> {

                        var salesStaff = loginResponse.data!!.SalesStaff[0]
                        prefrenceRepository.setSalesAgentData(
                            salesStaff
                        )

                      /*  prefrenceRepository.setMarketingRepID(
                            loginResponse.data!!.SalesStaff[0].MarketingRepID.toString()
                        )*/
                        prefrenceRepository.setAuthKeyData(
                            loginResponse.data!!.Auth_Key[0].Auth_Key
                        )

                        prefrenceRepository.setLogin()

                        _resp.value =
                            ValidateState.Success(
                                prefrenceRepository.getSalesAgentData(), loginResponse.msg!!
                            )

                    }

                }

            }
        }
    }


}