package com.gmlab.team_benew.main.home

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HomeRetrofitInterface {

    // profile 데이터 가져오기
    @GET("profile/{memberId}")
    fun getProfile(
        @Header("Authorization") bearertoken: String,
        @Path("memberId") memberId: Int
    ): Call<getProfilePreiviewData>

    // 메인 프로젝트 데이터 가져오기
    @GET("api/get/project/main-page/{userId}")
    fun getMainProject(
        @Header("Authorization") bearertoken: String,
        @Path("userId") userId: Int
    ): Call<getMainProjectData>
}