package com.gmlab.team_benew.project

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectListService (private val context: Context) {
    private lateinit var projectListView: ProjectListView

    fun setProjectListView(projectListView: ProjectListView) {
        this.projectListView = projectListView
    }

    fun getProjects() {
        val token = getTokenFromSharedPreferences()
        val userId = getIdFromSharedPreferences()

        if (token != null && userId != null) {
            val projectService = getRetrofit().create(ProjectListRetrofitInterface::class.java)
            val bearerToken = "Bearer $token"
            projectService.getProjects(bearerToken, userId).enqueue(object :
                Callback<List<ProjectResponse>> {
                override fun onResponse(call: Call<List<ProjectResponse>>, response: Response<List<ProjectResponse>>) {
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                if (it.isNotEmpty()) {
                                    projectListView.onProjectListSuccess(it)
                                } else {
                                    projectListView.onProjectListEmpty()
                                }
                            } ?: run {
                                projectListView.onProjectListEmpty()
                            }
                        }
                        401 -> {
                            projectListView.onProjectListFailure()
                        }
                        403 -> {
                            projectListView.onProjectListForbidden()
                        }
                        404 -> {
                            projectListView.onProjectListNotFound()
                        }
                        else -> {
                            projectListView.onProjectListFailure()
                        }
                    }
                }

                override fun onFailure(call: Call<List<ProjectResponse>>, t: Throwable) {
                    projectListView.onProjectListFailure()
                }
            })
        } else {
            projectListView.onProjectListFailure()
        }
    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }

    private fun getIdFromSharedPreferences(): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 }
    }
}