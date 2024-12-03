package com.gmlab.team_benew.profile

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface postProfileDetailRequest {
    @POST("profile/{memberId}")
    fun postProfile(
        @Header("Authorization") token: String,
        @Path("memberId") memberId: Int,
        @Body request: postProfileDetailData): Call<ResponseBody>
}