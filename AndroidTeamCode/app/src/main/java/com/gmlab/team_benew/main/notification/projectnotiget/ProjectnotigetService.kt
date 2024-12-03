package com.gmlab.team_benew.main.notification.projectnotiget

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectnotigetService(private val context: Context) {
    private lateinit var projectnotigetView: ProjectnotigetView



    fun setProjectnotiView(projectnotigetView: ProjectnotigetView){
        this.projectnotigetView = projectnotigetView
    }

    fun getProjectNoti(projectId: Int){
        val token = getTokenFromSharedPreferences()
        if (token != null) {
            val apiService = getRetrofit().create(ProjectnotigetRetrofitInterface::class.java)
            val bearerToken = "Bearer $token"
            apiService.getProjectDetail(bearerToken, projectId).enqueue(object :
                Callback<GetProjectNotiResponse> {
                override fun onResponse(
                    call: Call<GetProjectNotiResponse>,
                    response: Response<GetProjectNotiResponse>
                ) {
                    when (response.code()) {
                        200 -> response.body()
                            ?.let { projectnotigetView.onProjectNotiSuccess(it) }

                        401 -> projectnotigetView.onProjectNotiFailure(401)
                        403 -> projectnotigetView.onProjectNotiFailure(403)
                        404 -> projectnotigetView.onProjectNotiFailure(404)
                        else -> projectnotigetView.onProjectNotiFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<GetProjectNotiResponse>, t: Throwable) {
                    projectnotigetView.onProjectNotiFailure(500)
                }
            })

        }

    }

    fun getTokenFromSharedPreferences():String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs",Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken",null)
    }
}