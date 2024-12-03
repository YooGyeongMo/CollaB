package com.gmlab.team_benew.profile

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface getProfileDetailRequest {
    @GET("profile/{memberId}")
    fun getProfile(
        @Header("Authorization") token: String,
        @Path("memberId") memberId: Int
    ): Call<getProfileDetailData>
}