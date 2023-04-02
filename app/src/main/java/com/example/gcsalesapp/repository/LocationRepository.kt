package com.example.gcsalesapp.repository

import com.example.gcsalesapp.api.ApiResponseAny
import com.example.gcsalesapp.api.GcSalesApis
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.Constants.Companion.EXCEPTION_ERROR
import com.example.gcsalesapp.data.request.CallRequest
import com.example.gcsalesapp.data.request.SaveSalesStaffDeviceLocationRequest
import com.example.gcsalesapp.data.response.SaveLocationResponse
import com.google.gson.Gson
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LocationRepository @Inject constructor(
    private val apis: GcSalesApis
) {

    suspend fun saveSalesStaffDeviceLocation(requestData: SaveSalesStaffDeviceLocationRequest): Response<SaveLocationResponse?> {
        return try {
            val response = apis.saveSalesStaffDeviceLocation(requestData)

            if (response.isSuccessful) {

                if (response.body()!!.rs == 0) {

//                    Utill.print("response body ==="+Gson().toJson(response.body()))
                    val resp = Gson().fromJson(Gson().toJson(response.body()!!.data),SaveLocationResponse::class.java)
                    Response.Success("${response.body()!!.msg}",response.body()!!.rs, resp)
                } else {
                    Response.Error("${response.body()!!.msg}",response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}",response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}",EXCEPTION_ERROR)
        }

    }

    suspend fun callToDealer(requestData: CallRequest): Response<ApiResponseAny?>{
        return try {
            val response = apis.initiateCall(requestData)
            if (response.isSuccessful) {
                if (response.body()!!.rs == 0) {

//                    Utill.print("response body ==="+Gson().toJson(response.body()))
//                    val resp = Gson().fromJson(Gson().toJson(response.body()!!.data),SaveLocationResponse::class.java)
                    Response.Success("${response.body()!!.msg}",response.body()!!.rs, response.body())
                } else {
                    Response.Error("${response.body()!!.msg}",response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}",response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}",EXCEPTION_ERROR)
        }

    }

}