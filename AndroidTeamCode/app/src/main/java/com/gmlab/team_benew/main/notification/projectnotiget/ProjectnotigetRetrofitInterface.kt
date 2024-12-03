package com.gmlab.team_benew.main.notification.projectnotiget

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProjectnotigetRetrofitInterface {

    @GET("/api/get/project/{projectId}/detail")
    fun getProjectDetail(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: Int
    ): Call<GetProjectNotiResponse>
}