package com.gmlab.team_benew.chat.socket

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface ChatLogRetrofitInterface {
    @GET("/chat/message/{sendDate}/{roomId}")
    fun getChatTodayLog(@Header("Authorization") bearerTorken: String,
                        @Path("sendDate") nowDate: String,
                        @Path("roomId") roomId: String
    ): Call<MutableList<onMessageData>>
}