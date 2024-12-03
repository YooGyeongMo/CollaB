package com.gmlab.team_benew.chat.socket

import com.google.gson.annotations.SerializedName

data class getChatHistoryData(
    @SerializedName("message") val message: String,
    @SerializedName("roomId") val roomId: String,
    @SerializedName("sendDate") val sendDate: String,
    @SerializedName("sender") val sender: Int,
    @SerializedName("senderName") val senderName: String,
    @SerializedName("sequence") val sequence: Int
)
