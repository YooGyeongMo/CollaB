package com.gmlab.team_benew.project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProjectListRetrofitInterface {
    //Project List 받아오기
    @GET("/api/get/project/include/{userId}")
    fun getProjects(
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: Int
    ): Call<List<ProjectResponse>>

}