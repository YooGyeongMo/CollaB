package com.gmlab.team_benew.matching.projectmatchingfind

import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ProjectFindingAllGetService {

    suspend fun getAllProject(token: String, view: ProjectFindingAllGetView) {
        val retrofit = getRetrofit().create(ProjectFindingAllRetrofitInterface::class.java)
        try {
            Log.d("ProjectService", "네트워크 통신 성공 !!")
            val response = withContext(Dispatchers.IO) {
                retrofit.getProjects("Bearer $token")
            }
            when (response.code()) {
                200 -> {
                    Log.d("ProjectService", "200 통신 성공 !")
                    response.body()?.let { view.onSuccessProjectFindingGet(it) }
                }

                401 -> {
                    Log.e("ProjectService", "Unauthorized access")
                    view.onUnauthorized("Unauthorized access.")
                }

                403 -> {
                    Log.e("ProjectService", "Forbidden access")
                    view.onForbidden("Forbidden access.")
                }

                404 -> {
                    Log.e("ProjectService", "Not found")
                    view.onNotFound("Not found.")
                }

                else -> {
                    Log.e("ProjectService", "Unexpected error: ${response.code()}")
                    view.onFailureProjectFindingGet("Unexpected error: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("ProjectService", "Exception: ${e.message}")
            view.onFailureProjectFindingGet(e.message ?: "Unknown error occurred")
        }
    }
}

