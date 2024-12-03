package com.gmlab.team_benew.chat.chatintro.friendalarm

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FriendAlarmRetrofitInterface {
    @POST("/alarms")
    suspend fun createAlarm(
        @Header("Authorization") token: String,
        @Body alarmRequest: FriendAlarmRequest
    ): Response<FriendAlarmResponse>
}