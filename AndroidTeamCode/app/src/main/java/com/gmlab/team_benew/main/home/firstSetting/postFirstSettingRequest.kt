package com.gmlab.team_benew.main.home.firstSetting

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface postFirstSettingRequest {
    @POST("profile/{memberId}")
    fun postFirstSetting(
        @Header("Authorization") token: String,
        @Path("memberId") memberId: Int,
        @Body request: postFirstSettingData
    ): Call<ResponseBody>
}