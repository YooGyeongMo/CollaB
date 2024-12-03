package com.gmlab.team_benew.chat.chatintro.friendalarm

import com.google.gson.annotations.SerializedName

data class FriendAlarmRequest(
    @SerializedName("message") val message: String,
    @SerializedName("receiverUserId") val receiverUserId: Long,
    @SerializedName("senderUserId") val senderUserId: Long
)