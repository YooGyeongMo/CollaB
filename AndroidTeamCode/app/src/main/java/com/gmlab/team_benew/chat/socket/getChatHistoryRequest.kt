package com.gmlab.team_benew.chat.socket

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface getChatHistoryRequest {
    @GET("/message")
    fun getMessages(
        @Header("Authorization") token: String,
        @Query("roomId") roomId: String,
        @Query("sendDate") sendDate: String
    ): Call<List<getChatHistoryData>>
}