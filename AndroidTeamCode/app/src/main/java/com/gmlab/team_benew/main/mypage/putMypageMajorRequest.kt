package com.gmlab.team_benew.main.mypage

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface putMypageMajorRequest {
    @PUT("/user/update/{id}")
    fun putMypageMajor(
        @Header("Authorization") token: String,
        @Path("id") memberId: Int,
        @Body request: putMypageMajorData
    ): Call<Boolean>
}