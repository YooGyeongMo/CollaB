package com.gmlab.team_benew.main.notification.freindaccept

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendAcceptRetroInterface {
    @POST("/friend/accept")
    suspend fun acceptFriend(
        @Header("Authorization") token: String,
        @Query("friendId") friendId: Long,
        @Query("memberId") memberId: Long
    ): Response<Void>
}
