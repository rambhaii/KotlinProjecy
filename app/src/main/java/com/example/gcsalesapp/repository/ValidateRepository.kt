package com.example.gcsalesapp.repository

import com.example.gcsalesapp.api.GcSalesApis
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.request.ValidateSalesPersonRequest
import com.example.gcsalesapp.data.response.SaveLocationResponse
import com.example.gcsalesapp.data.response.ValidateSalesPersonResponse
import com.google.gson.Gson
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ValidateRepository @Inject constructor(
    private val apis: GcSalesApis
) {

    suspend fun validateSalesLogin(requestData: ValidateSalesPersonRequest): Response<ValidateSalesPersonResponse?> {
        return try {
            Utill.print("isSuccessful = 0")
            val response = apis.validateMarketingRepDayPassword(requestData)

            if (response.isSuccessful) {
                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(
                        Gson().toJson(response.body()!!.data),
                        ValidateSalesPersonResponse::class.java)
                    Response.Success(response.message(),response.body()!!.rs, resp)
                } else {
                    Response.Error("${response.body()!!.msg}",response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}",response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", Constants.EXCEPTION_ERROR)
        }


    }

}