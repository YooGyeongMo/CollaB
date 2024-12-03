package com.gmlab.team_benew.project.projectgetdetail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProjectDetailRetrofitInterface {
    @GET("/api/get/project/{projectId}/detail")
    fun getProjectDetail(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: Int
    ): Call<GetProjectDeatilResponse>
}