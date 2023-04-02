package com.example.gcsalesapp.repository

import com.example.gcsalesapp.api.GcSalesApis
import com.example.gcsalesapp.api.Response
import com.example.gcsalesapp.data.Constants
import com.example.gcsalesapp.data.Constants.Companion.EXCEPTION_ERROR
import com.example.gcsalesapp.data.request.*
import com.example.gcsalesapp.data.response.*
import com.google.gson.Gson
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DealerRepository @Inject constructor(
    private val apis: GcSalesApis
) {

    suspend fun getDealerListByEngagementID(requestData: DealerEngagementStatusRequest): Response<DealerEngagementStatusResponse?> {
        return try {
            val response = apis.getDealerEngagementStatus(requestData)

            if (response.isSuccessful) {

                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(
                        Gson().toJson(response.body()!!.data),
                        DealerEngagementStatusResponse::class.java
                    )
//                    if(resp.Dealers.size>0) {
                        Response.Success(response.message(), response.body()!!.rs, resp)
//                    }else{
//                        Response.Error("No dealers found!", response.body()!!.rs)
//                    }
                } else {
                    Response.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", EXCEPTION_ERROR)
        }

    }


    suspend fun geOrderListByDealerID(requestData: DealerEngagementStatusRequest): Response<DealerOrderListResponse?> {
        return try {
            val response = apis.getDealerOrderDetailForSalesStaff(requestData)
            if (response.isSuccessful) {
                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(
                        Gson().toJson(response.body()!!.data),
                        DealerOrderListResponse::class.java
                    )
                    Response.Success(response.message(), response.body()!!.rs, resp)
                } else {
                    Response.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", EXCEPTION_ERROR)
        }

    }

    suspend fun setDealerEstimateVolume(requestData: DealerEngagementStatusRequest): Response<UpdateDealerEngagementEstimatedVolumeResponse?> {
        return try {
            val response = apis.updateDealerEngagementEstimatedVolume(requestData)
            if (response.isSuccessful) {
                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(Gson().toJson(response.body()!!.data),UpdateDealerEngagementEstimatedVolumeResponse::class.java)
                    Response.Success(response.message(), response.body()!!.rs, resp)
                } else {
                    Response.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", EXCEPTION_ERROR)
        }

    }


    suspend fun saveMechanicForDealer(requestData: SaveDealerAssociatedMechanicDataRequest): Response<SaveDealerAssociatedMechanicDataResponse?> {
        return try {
            val response = apis.saveDealerAssociatedMechanicDataRaw(requestData)

            if (response.isSuccessful) {

                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(Gson().toJson(response.body()!!.data),SaveDealerAssociatedMechanicDataResponse::class.java)
                    Response.Success(response.message(), response.body()!!.rs,resp)
                } else {
                    Response.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", EXCEPTION_ERROR)
        }

    }

    suspend fun registerDealer(requestData: RegisterNewDealerBySalesStaff): Response<RegisterDealerResponse?> {
        return try {
            val response = apis.registerNewDealerBySalesStaff(requestData)

            if (response.isSuccessful) {

                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(Gson().toJson(response.body()!!.data),RegisterDealerResponse::class.java)
                    Response.Success(response.message(), response.body()!!.rs,resp)
                } else {
                    Response.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", EXCEPTION_ERROR)
        }

    }

    suspend fun getMechanicTypeList(requestData: MarketingRep): Response<GetMechanicVehicleTypeResponse?> {
        return try {
            val response = apis.getMechanicVehicleType(requestData)

            if (response.isSuccessful) {

                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(Gson().toJson(response.body()!!.data),GetMechanicVehicleTypeResponse::class.java)
                    Response.Success(response.message(), response.body()!!.rs,resp)
                } else {
                    Response.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", EXCEPTION_ERROR)
        }

    }

    suspend fun getMechanics(requestData: GetMechanicForSalesRepRequest): Response<GetMechanicForSalesRepResponse?> {
        return try {
            val response = apis.getMechanicForSalesRep(requestData)

            if (response.isSuccessful) {

                if (response.body()!!.rs == 0) {
                    val resp = Gson().fromJson(Gson().toJson(response.body()!!.data),GetMechanicForSalesRepResponse::class.java)
                    Response.Success(response.message(), response.body()!!.rs,resp)
                } else {
                    Response.Error("${response.body()!!.msg}", response.body()!!.rs)
                }
            } else {
                Response.Error("${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Response.Error("Network error-${Constants.EXCEPTION_ERROR}", EXCEPTION_ERROR)
        }

    }


}