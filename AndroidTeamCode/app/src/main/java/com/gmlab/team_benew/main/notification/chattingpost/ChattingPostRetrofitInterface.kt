package com.gmlab.team_benew.main.notification.chattingpost

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class ChatUser(
    val userId: Long,
    val userName: String
)

interface ChattingPostRetrofitInterface {

    @POST("chat/new")
    fun chatPost(@Header("Authorization") beaererToken: String,
                 @Body users: List<ChatUser>
    ): Call<ChattingPostResponse>
}