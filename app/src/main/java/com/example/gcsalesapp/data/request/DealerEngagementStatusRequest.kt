package com.example.gcsalesapp.data.request

data class DealerEngagementStatusRequest(
    val engagementStatusID: Int,
    val EstimatedVolume: Double,
    val PageNumber: Int,
    val PinCode: String,
    val SearchText: String,
    val dealerID: Int,
    val marketingRep: MarketingRep
)