package com.gmlab.team_benew.project.projectgetdetail

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectDetailService(private val context: Context) {
    private lateinit var projectDetailView: ProjectDetailView

    fun setProjectDetailView(projectDetailView: ProjectDetailView) {
        this.projectDetailView = projectDetailView
    }

    fun getProjectDetail(projectId: Int) {
        val token = getTokenFromSharedPreferences()
        if (token != null) {
            val apiService = getRetrofit().create(ProjectDetailRetrofitInterface::class.java)
            val bearerToken = "Bearer $token"
            apiService.getProjectDetail(bearerToken, projectId).enqueue(object : Callback<GetProjectDeatilResponse> {
                    override fun onResponse(
                        call: Call<GetProjectDeatilResponse>,
                        response: Response<GetProjectDeatilResponse>
                    ) {
                        when (response.code()) {
                            200 -> response.body()
                                ?.let { projectDetailView.onProjectDetailSuccess(it) }

                            401 -> projectDetailView.onProjectDetailFailure(401)
                            403 -> projectDetailView.onProjectDetailFailure(403)
                            404 -> projectDetailView.onProjectDetailFailure(404)
                            else -> projectDetailView.onProjectDetailFailure(response.code())
                        }
                    }

                    override fun onFailure(call: Call<GetProjectDeatilResponse>, t: Throwable) {
                        projectDetailView.onProjectDetailFailure(500)
                    }
                })

        }
    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }

}