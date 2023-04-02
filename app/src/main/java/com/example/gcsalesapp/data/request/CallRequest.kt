package com.example.gcsalesapp.data.request

data class CallRequest(
    val company_id: String,
    val number: String,
    val public_ivr_id: String,
    val reference_id: String,
    val secret_token: String,
    val type: String,
    val user_id: String
)