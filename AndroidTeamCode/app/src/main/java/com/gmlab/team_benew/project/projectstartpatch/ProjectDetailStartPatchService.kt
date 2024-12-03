package com.gmlab.team_benew.project.projectstartpatch

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectDetailStartPatchService(private val context: Context) {
    private val retrofit = getRetrofit()

    private val api = retrofit.create(ProjectDetailStartPatchRetrofitInterface::class.java)

    fun patchProjectStart(projectId: Int, projectDeadlineDate: String, view: ProjectDetailStartPatchView) {
        val token = getTokenFromSharedPreferences()
        if (token != null) {
            val bearerToken = "Bearer $token"
            val requestBody = mapOf("projectDeadlineDate" to projectDeadlineDate)
            api.patchProjectStart(bearerToken, projectId, requestBody).enqueue(object : Callback<ProjectDetailStartPatchResponse> {
                override fun onResponse(call: Call<ProjectDetailStartPatchResponse>, response: Response<ProjectDetailStartPatchResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            view.onSuccessProjectStartPatch(body)
                        } else {
                            view.onFailureProjectStartPatch("Response body is null")
                        }
                    } else {
                        when (response.code()) {
                            401 -> view.onFailureProjectStartPatch("Unauthorized")
                            403 -> view.onFailureProjectStartPatch("Forbidden")
                            else -> view.onFailureProjectStartPatch("Unexpected Error: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<ProjectDetailStartPatchResponse>, t: Throwable) {
                    Log.e("ProjectDetailStartPatch", "PATCH 요청 실패", t)
                    view.onFailureProjectStartPatch("Request Failed: ${t.message}")
                }
            })
        } else {
            view.onFailureProjectStartPatch("Token not found")
        }
    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }
}
