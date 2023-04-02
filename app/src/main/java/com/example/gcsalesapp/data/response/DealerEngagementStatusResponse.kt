package com.example.gcsalesapp.data.response

data class DealerEngagementStatusResponse(
    val Dealers: List<Dealer>,
    val TotalDealerCount: Int,
    val Summary: List<Summary>
)