package com.gmlab.team_benew.main

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MainLiveGetAlarmsRetrofitInterface {
    @GET("/alarms/readnum/{userId}")
    fun liveAlarmGet(@Header("Authorization") bearerToken: String, @Path("userId") userId: Int): Call<ResponseBody>
}

