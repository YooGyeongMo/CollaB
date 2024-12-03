package com.gmlab.team_benew.chat.socket

data class ChatMessage(
    val sender: String,
    val message: String,
    val userId: Int,
    val senderId: Int?,
    val senddate : String?
)