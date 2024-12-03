package com.gmlab.team_benew.chat.chatintro.friendadd

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendAddRetrofitInterface {
    @POST("/friend/add")
    suspend fun addFriend(
        @Header("Authorization") token: String,
        @Query("friendId") friendId: Long,
        @Query("memberId") memberId: Long
    ): Response<Void>
}