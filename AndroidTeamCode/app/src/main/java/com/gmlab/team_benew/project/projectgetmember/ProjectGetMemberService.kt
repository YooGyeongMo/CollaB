package com.gmlab.team_benew.project.projectgetmember

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectGetMemberService(private val context: Context) {

    interface ProjectGetMemberCallback {
        fun onSuccess(members: List<ProjectMemberResponse>)
        fun onFailure(errorCode: Int)
    }

    fun getProjectMembers(projectId: Int, callback: ProjectGetMemberCallback) {
        val token = getTokenFromSharedPreferences()
        if (token != null) {
            val apiService = getRetrofit().create(ProjectGetMemberRetrofitInterface::class.java)
            val bearerToken = "Bearer $token"
            apiService.getProjectMemebers(bearerToken, projectId).enqueue(object : Callback<MutableList<ProjectMemberResponse>> {
                override fun onResponse(
                    call: Call<MutableList<ProjectMemberResponse>>,
                    response: Response<MutableList<ProjectMemberResponse>>
                ) {
                    when (response.code()) {
                        200 -> response.body()?.let { callback.onSuccess(it) }
                        401 -> callback.onFailure(401)
                        403 -> callback.onFailure(403)
                        404 -> callback.onFailure(404)
                        else -> callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<MutableList<ProjectMemberResponse>>, t: Throwable) {
                    callback.onFailure(500)
                }
            })
        } else {
            callback.onFailure(401) // Unauthorized
        }
    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }
}
