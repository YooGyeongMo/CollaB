package com.gmlab.team_benew.chat.retrofit

import com.google.gson.annotations.SerializedName

data class ChatRoomListModelItem(
    @SerializedName("name")
    val name:String,
    @SerializedName("roomId")
    val roomId:String
)
