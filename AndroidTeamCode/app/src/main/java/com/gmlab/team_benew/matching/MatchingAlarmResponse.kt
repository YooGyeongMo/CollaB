package com.gmlab.team_benew.matching

import com.google.gson.annotations.SerializedName

data class MatchingAlarmResponse (

    @SerializedName("id")
    val id: Long,

    @SerializedName("message")
    val message: String,

    @SerializedName("read")
    val read: Boolean,

    @SerializedName("receiverUserId")
    val receiverUserId: Long,

    @SerializedName("senderUserId")
    val senderUserId: Long

)