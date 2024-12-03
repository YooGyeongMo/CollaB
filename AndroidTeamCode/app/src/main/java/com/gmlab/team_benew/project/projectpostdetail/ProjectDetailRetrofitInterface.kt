package com.gmlab.team_benew.project.projectpostdetail

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

public interface ProjectDetailRetrofitInterface {
    @POST("api/post/project")
    fun postNewProject(
        @Header("Authorization") token: String,
        @Body postProjectDeatilRequest: PostProjectDetailRequest
    ): Call<Void>
}
