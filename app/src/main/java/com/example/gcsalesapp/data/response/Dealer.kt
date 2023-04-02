package com.example.gcsalesapp.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dealer(
    val CalculatedVolPerMonth: Double,
    val DealerEngagementID: Int,
    val DealerID: Double,
    val DealershipName: String,
    val DealershipType: String,
    val DistributorName: String,
    val District: String,
    val EngagementStatusColourCode: String,
    val OrderColorCode: String,
    val EngagementStatusID: Int,
    val EngagementStatusIMgUrl: String,
    val EngagementStatusName: String,
    val EstimatedVolPerMonth: Double,
    val MarketingRep: String,
    val MarketingRepID: Int,
    val MonthsVolumeInLit: Double,
    val OpenVisitPage: Boolean,
    val PINCode: String,
    val PhoneNo: String,
    val VisitCount: Int,
    val Remark: String
): Parcelable