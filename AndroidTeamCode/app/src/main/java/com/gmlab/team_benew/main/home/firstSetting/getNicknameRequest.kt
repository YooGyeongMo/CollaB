package com.gmlab.team_benew.main.home.firstSetting

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface getNicknameRequest {
    @GET("profile/{memberId}")
    fun getNickname(
        @Header("Authorization") token: String,
        @Path("memberId") memberId: Int
    ): Call<getNicknameData>
}