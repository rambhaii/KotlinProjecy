package com.example.gcsalesapp.data.request

data class SaveSalesStaffDeviceLocationRequest(
    val Latitude: Double,
    val Longitude: Double,
    val marketingRep: MarketingRep
)