package com.example.gcsalesapp.ui.viewmodal

import android.location.Address
import android.location.Geocoder
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants.Companion.FASTEST_LOCATION_INTERVAL
import com.example.gcsalesapp.data.request.SaveSalesStaffDeviceLocationRequest
import com.example.gcsalesapp.data.response.SaveLocationResponse
import com.example.gcsalesapp.repository.LocationRepository
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.repository.Utill
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.*


class TrackingViewModel @ViewModelInject constructor(
    private val repository: LocationRepository,
    private val geocoder: Geocoder,
    private val preference: PrefrenceRepository
) : ViewModel() {


    private val _addressresp = MutableLiveData<ApiState<String>>()
    val validateAddressResp: LiveData<ApiState<String>>
        get() = _addressresp


    private val _locationresp = MutableLiveData<ApiState<String>>()
    val validateLocationResp: LiveData<ApiState<String>>
        get() = _locationresp


    companion object {
        var interval = FASTEST_LOCATION_INTERVAL
    }


    public fun getAddress(latlang: LatLng) {
        val addresses: List<Address>

//        sendLocationToServer(latlang)

        addresses = geocoder.getFromLocation(
            latlang.latitude,
            latlang.longitude,
            1
        )

        Utill.print("addresses == ${addresses}")

        val address: String =
            addresses[0].getAddressLine(0)

//        val city: String = addresses[0].getLocality()
//        val state: String = addresses[0].getAdminArea()
//        val country: String = addresses[0].getCountryName()
        val postalCode: String = addresses[0].getPostalCode()?:""
//        val knownName: String = addresses[0].getFeatureName()


        _addressresp.value = ApiState.Success(address, postalCode)

    }


    public fun sendLocationToServer(latlang: LatLng) {

        viewModelScope.launch {

            Utill.print("interval time ==${Date()}")

            val saveSalesStaffDeviceLocationRequest = SaveSalesStaffDeviceLocationRequest(
                latlang.latitude,
                latlang.longitude,
                preference.getMarketingRepData()
            )

            when (val loginResponse =
                repository.saveSalesStaffDeviceLocation(saveSalesStaffDeviceLocationRequest)) {

                is Response.Error -> _locationresp.value =
                    ApiState.Failure(loginResponse.msg!!, loginResponse.code)

                is Response.Success -> {

//                    preference.setLocationInterval(loginResponse.data!!.data.last().Interval)

//                    val resdata = loginResponse.data!!.data as SaveLocationResponse
//
                    interval = loginResponse.data!!.last().Interval.times(1000L)

//                    _resp.value =
//                        ApiState.Success(LocationData("", latlang), loginResponse.data!!.msg)

                }


            }

        }

    }

}