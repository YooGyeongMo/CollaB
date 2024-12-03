package com.gmlab.team_benew.main.notification.matchingalarm

import com.gmlab.team_benew.matching.MatchingAlarmResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MatchingAlarmsReadRetrofitInterface {

    //알람매칭수락 동기로 바꿈 PutAlarams과 동기적으로 작동해야함 그래서 Call로 받는것이 아닌Reponse로 던져줌
    @PATCH("/api/patch/match/success/{sender}/{receiver}")
    fun matchingAlarmsAccess(
        @Header("Authorization") bearerToken: String,
        @Path("sender") sender: Long,
        @Path("receiver") receiver: Long): Call<MatchingAlarmResponse>
    //        @Path("receiver") receiver: Long): Call<MatchingAlarmResponse> 비동기

    //알람매칭거절 동기로 바꿈 PutAlarams과 동기적으로 작동해야함 그래서 Call로 받는것이 아닌Reponse로 던져줌
    @PATCH("/api/patch/match/false/{sender}/{receiver}")
    fun matchingAlarmsReject(
        @Header("Authorization") bearerToken: String,
        @Path("sender") sender: Long,
        @Path("receiver") receiver: Long): Call<MatchingAlarmResponse>
//        @Path("receiver") receiver: Long): Call<MatchingAlarmResponse> 비동기
}