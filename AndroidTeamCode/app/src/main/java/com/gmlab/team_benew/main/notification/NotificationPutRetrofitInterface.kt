package com.gmlab.team_benew.main.notification

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationPutRetrofitInterface {
    //동기로 처리 그래서 Call 이 아닌 Response로
    @PUT("/alarms/{userId}/{alarmId}/read")
    fun alarmsPut(@Header("Authorization") bearerToken: String,
                  @Path("userId") userId: Long,
                  @Path("alarmId") alarmId: Long): Call<ResponseBody>

}
