package com.gmlab.team_benew.chat.chatintro.friendalarm

import com.google.gson.annotations.SerializedName

data class FriendAlarmResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("message") val message: String,
    @SerializedName("read") val read: Boolean,
    @SerializedName("receiverUserId") val receiverUserId: Long,
    @SerializedName("senderUserId") val senderUserId: Long,
    @SerializedName("timestamp") val timestamp: String
)