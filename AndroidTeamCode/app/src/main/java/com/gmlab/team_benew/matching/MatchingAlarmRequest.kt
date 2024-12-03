package com.gmlab.team_benew.matching

import com.google.gson.annotations.SerializedName

data class MatchingAlarmRequest (
    @SerializedName("message")
    val message: String,

    @SerializedName("receiverUserId")
    val receiverId: Long,

    @SerializedName("senderUserId")
    val myUserId: Long
)