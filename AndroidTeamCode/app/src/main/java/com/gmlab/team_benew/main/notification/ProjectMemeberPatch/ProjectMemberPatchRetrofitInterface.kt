package com.gmlab.team_benew.main.notification.ProjectMemeberPatch

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProjectMemberPatchRetrofitInterface {

    @PATCH("api/patch/project/{projectId}/member/{userId}")
    fun addProjectMember(
        @Header("Authorization") bearerToken: String,
        @Path("projectId") projectId: Long,
        @Path("userId") userId: Long
    ): Call<Void>
}