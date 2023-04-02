package com.example.gcsalesapp.data.request

data class GetMechanicForSalesRepRequest(
    val DealerID: Int,
    val MarketingRepID: String,
    val PageNumber: Int,
    val RowsOfPage: Int
)