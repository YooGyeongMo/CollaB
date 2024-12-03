package com.gmlab.team_benew.chat.socket

data class onMessageData(
    val type: String,
    val roomId: String,
    val sender: Int,
    val senderName: String,
    val message: String,
    val senddate: String
)
