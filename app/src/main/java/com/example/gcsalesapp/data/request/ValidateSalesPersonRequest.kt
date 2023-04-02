package com.example.gcsalesapp.data.request

data class ValidateSalesPersonRequest(
    val AppMinorVersion: Int,
    val AppVersion: Int,
    val DeviceID: String,
    val LanguageID: Int,
    val MachineID: String,
    val OSIndex: Int,
    val OTP: String,
    val PhoneNo: String
)