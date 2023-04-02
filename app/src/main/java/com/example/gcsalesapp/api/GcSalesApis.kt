package com.example.gcsalesapp.api

import com.example.gcsalesapp.data.request.*
import com.example.gcsalesapp.data.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GcSalesApis {


//    http://udev.scci.co.in:88/MarketingAppServicesV2/api/Auth/ValidateMarketingRepDayPassword

    @POST(value = "api/Auth/ValidateMarketingRepDayPassword")
    suspend fun validateMarketingRepDayPassword(
        @Body requestData : ValidateSalesPersonRequest
    ):Response<ApiResponseAny>

//    http://udev.scci.co.in:88/MarketingAppServicesV2/api/Engagement/GetDealerEngagementStatus

    @POST(value = "api/Engagement/GetDealerEngagementStatus")
    suspend fun getDealerEngagementStatus(
        @Body requestData : DealerEngagementStatusRequest
    ):Response<ApiResponseAny>

//    http://udev.scci.co.in:88/MarketingAppServicesV2/api/Engagement/GetDealerOrderDetailForSalesStaff

    @POST(value = "api/Engagement/GetDealerOrderDetailForSalesStaff")
    suspend fun getDealerOrderDetailForSalesStaff(
        @Body requestData : DealerEngagementStatusRequest
    ):Response<ApiResponseAny>

//    http://udev.scci.co.in:88/MarketingAppServicesV2/api/Location/SaveSalesStaffDeviceLocation

    @POST(value = "api/Location/SaveSalesStaffDeviceLocation")
    suspend fun saveSalesStaffDeviceLocation(
        @Body requestData : SaveSalesStaffDeviceLocationRequest
    ):Response<ApiResponseAny>


    //    http://udev.scci.co.in:88/MarketingAppServicesV2/api/DealerRegistration/UpdateDealerEngagementEstimatedVolume

    @POST(value = "api/DealerRegistration/UpdateDealerEngagementEstimatedVolume")
    suspend fun updateDealerEngagementEstimatedVolume(
        @Body requestData : DealerEngagementStatusRequest
    ):Response<ApiResponseAny>


//    http://udev.scci.co.in:88/MarketingAppServicesV2/api/Mechanic/SaveDealerAssociatedMechanicData_Raw

    @POST(value = "api/Mechanic/SaveDealerAssociatedMechanicData_Raw")
    suspend fun saveDealerAssociatedMechanicDataRaw(
        @Body requestData : SaveDealerAssociatedMechanicDataRequest
    ):Response<ApiResponseAny>

//    http://udev.scci.co.in:88/MarketingAppServicesV2/api/DealerRegistration/RegisterNewDealerBySalesStaff_V2


    @POST(value = "api/DealerRegistration/RegisterNewDealerBySalesStaff_V2")
    suspend fun registerNewDealerBySalesStaff(
        @Body requestData : RegisterNewDealerBySalesStaff
    ):Response<ApiResponseAny>


//    https://gc9.scci.co.in/GameZone/api/Game/InitiateCall

    @POST(value = "api/Calling/InitiateCall")
    suspend fun initiateCall(
        @Body requestData : CallRequest
    ):Response<ApiResponseAny>

//    http://udev.scci.co.in:88/MV2/api/Mechanic/GetMechanicVehicleType

    @POST(value = "api/Mechanic/GetMechanicVehicleType")
    suspend fun getMechanicVehicleType(
        @Body requestData : MarketingRep
    ):Response<ApiResponseAny>

//    http://udev.scci.co.in:88/MarketingAppServicesV2/api/Mechanic/GetMechanicForSalesRep

    @POST(value = "api/Mechanic/GetMechanicForSalesRep")
    suspend fun getMechanicForSalesRep(
        @Body requestData : GetMechanicForSalesRepRequest
    ):Response<ApiResponseAny>


//    http://udev.scci.co.in:88/MarketingAppServicesV2/webpages/DealerVisit.aspx?M=10109&D=352512&Auth_Key=5B7E725D-F879-4B19-8E39-3CFF41F0DBEC

}