package com.gmlab.team_benew.main.mypage

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface getMypageRequest {
    @GET("profile/{memberId}")
    fun getMypage(
        @Header("Authorization") token: String,
        @Path("memberId") memberId: Int
    ): Call<getMypageData>
}