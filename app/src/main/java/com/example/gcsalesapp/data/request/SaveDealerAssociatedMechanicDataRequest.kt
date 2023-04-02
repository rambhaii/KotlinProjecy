package com.example.gcsalesapp.data.request

import com.example.gcsalesapp.data.response.Summary

data class SaveDealerAssociatedMechanicDataRequest(
    val DealerID: Int,
    val marketingRep: MarketingRep,
    val mechanic: List<Mechanic>
)