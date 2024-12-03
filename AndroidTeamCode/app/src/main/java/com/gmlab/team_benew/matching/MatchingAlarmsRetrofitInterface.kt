package com.gmlab.team_benew.matching


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MatchingAlarmsRetrofitInterface {
    //알람 보내기 생성
    @POST("/alarms")
    fun matchingAlarmsSend(
        @Header("Authorization") bearerToken: String,
        @Body matchingAlarmRequest: MatchingAlarmRequest
    ): Call<MatchingAlarmResponse>
}