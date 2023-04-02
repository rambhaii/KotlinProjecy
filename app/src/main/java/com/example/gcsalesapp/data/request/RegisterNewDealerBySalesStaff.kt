package com.example.gcsalesapp.data.request

data class RegisterNewDealerBySalesStaff(
    val marketingRep: MarketingRep,
    val DealershipName: String,
    val MiddleName: String,
    val Name: String,
    val PhoneNo: String,
    val PinCode: String,
    val Surname: String
)