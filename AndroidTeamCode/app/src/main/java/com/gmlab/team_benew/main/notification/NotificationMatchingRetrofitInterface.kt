package com.gmlab.team_benew.main.notification


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NotificationMatchingRetrofitInterface {
    @GET("/alarms/{responseId}")
    fun alarmsGet(
        @Header("Authorization") bearerToken: String,
        @Path("responseId") responseId: Long
    ): Call<List<NotificationMatchingResponse>>
}
