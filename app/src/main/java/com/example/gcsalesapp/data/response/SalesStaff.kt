package com.example.gcsalesapp.data.response

data class SalesStaff(
    val BaseLocation: String,
    val Designation: String,
    val Email: String,
    val ISActive: Boolean,
    val IsManager: Boolean,
    val MarketingRepFName: String,
    val MarketingRepID: Int,
    val MarketingRepLName: String,
    val PhoneNo: String,
    val Salutation: String,
    val ServiceProvider: String,
    var CallUserID: String,
    val StateID: Int
)