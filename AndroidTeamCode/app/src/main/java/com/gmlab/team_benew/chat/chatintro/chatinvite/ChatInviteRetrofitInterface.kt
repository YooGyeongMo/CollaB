package com.gmlab.team_benew.chat.chatintro.chatinvite

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatInviteRetrofitInterface {
    @POST("/chat/new")
    suspend fun createChatRoom(
        @Header("Authorization") token: String,
        @Body request: List<ChatRoomRequest>
    ): Response<ChatRoomResponse>
}

data class ChatRoomRequest(
    val userId: Long,
    val userName: String
)

data class ChatRoomResponse(
    val createDate: CreateDate,
    val isProjectStarted: Int,
    val projectId: Long,
    val roomId: String,
    val roomName: String
)

data class CreateDate(
    val date: Int,
    val day: Int,
    val hours: Int,
    val minutes: Int,
    val month: Int,
    val nanos: Int,
    val seconds: Int,
    val time: Long,
    val timezoneOffset: Int,
    val year: Int
)