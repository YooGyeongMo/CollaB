package com.gmlab.team_benew.main.notification.ProjectMemeberPatch

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Response

class ProjectMemberPatchService {
    suspend fun addProjectMember(context: Context, projectId: Long, userId: Long): Response<Void> {
        val token = getTokenFromSharedPreferences(context)
        val bearerToken = "Bearer $token"
        val service = getRetrofit().create(ProjectMemberPatchRetrofitInterface::class.java)
        return service.addProjectMember(bearerToken, projectId, userId).execute()
    }

    private fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }
}
