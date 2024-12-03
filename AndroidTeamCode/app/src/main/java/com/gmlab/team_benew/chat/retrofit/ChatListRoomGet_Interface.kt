package com.gmlab.team_benew.chat.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ChatListRoomGet_Interface {
    @GET("/chat/rooms/{userId}")
    fun getChatRoomList(@Header("Authorization") token:String, @Path("userId") userId:Int
    ): Call<MutableList<ChatRoomListModelItem>>
}