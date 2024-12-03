package com.gmlab.team_benew.profile

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface getTechnologyRequest {
    @GET("technology-levels/member/{account}")
    fun getTechnology(
        @Header("Authorization") token: String,
        @Path("account") account: String
    ): Call<List<getTechnologyData>>
}