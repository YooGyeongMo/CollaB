package com.gmlab.team_benew.chat.chatintro


import ChatData
import FriendResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatIntroRetrofitInterface {
    @GET("/chat/rooms/{userId}")
    fun getChatRooms(
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: Int
    ): Call<List<ChatData>>

    @GET("/friend/list")
    fun getFriendsList(
        @Header("Authorization") bearerToken: String,
        @Query("memberId") memberId: Long
    ): Call<List<FriendResponse>>
}