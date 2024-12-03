package com.gmlab.team_benew.matching.projectmatchingfind

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProjectFindingAllRetrofitInterface {
    @GET("api/get/projects")
    suspend fun getProjects(
        @Header("Authorization") token:String)
        : Response<List<ProjectFindingAllResponse>>
}