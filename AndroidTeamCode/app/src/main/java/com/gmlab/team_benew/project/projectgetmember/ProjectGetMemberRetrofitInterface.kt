package com.gmlab.team_benew.project.projectgetmember


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProjectGetMemberRetrofitInterface {

    @GET("api/get/project/{projectId}/members")
    fun getProjectMemebers(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: Int
    ): Call<MutableList<ProjectMemberResponse>>
}