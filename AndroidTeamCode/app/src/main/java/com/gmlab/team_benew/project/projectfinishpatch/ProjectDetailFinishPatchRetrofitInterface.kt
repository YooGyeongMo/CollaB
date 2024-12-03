package com.gmlab.team_benew.project.projectfinishpatch

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProjectDetailFinishPatchRetrofitInterface {

    @PATCH("/api/patch/project/finish/{projectId}")
    fun patchProjectFinish(
        @Header("Authorization")token: String,
        @Path("projectId")projectId: Int,
        ) : Call<ProjectDetailFinishPatchResponse>

}