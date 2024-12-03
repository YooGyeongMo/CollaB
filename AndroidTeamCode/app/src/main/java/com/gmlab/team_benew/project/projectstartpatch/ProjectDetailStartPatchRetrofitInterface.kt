package com.gmlab.team_benew.project.projectstartpatch

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProjectDetailStartPatchRetrofitInterface {
    @PATCH("/api/patch/project/start/{projectId}")
    fun patchProjectStart(
        @Header("Authorization") token: String,
        @Path("projectId") projectId: Int,
        @Body projectDeadlineDto: Map<String, String>
    ): Call<ProjectDetailStartPatchResponse>
}
