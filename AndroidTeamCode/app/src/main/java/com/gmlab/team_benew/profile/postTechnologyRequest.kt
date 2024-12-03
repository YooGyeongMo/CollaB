package com.gmlab.team_benew.profile

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface postTechnologyRequest {
    @POST("technology-levels/member/{userId}")
    fun postTechnology(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body request: postTechnologyData): Call<Boolean>
}