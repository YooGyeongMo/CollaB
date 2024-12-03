package com.gmlab.team_benew.project.projectpostdetail

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectDetailPostService(
    private val context: Context,
    private val view: ProjectPostDetailView
) {


    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }

    private fun getIdFromSharedPreferences(): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getInt("loginId", -1).takeIf { it != -1 }
    }

    fun postProject(projectRequestDto: PostProjectDetailRequest) {
        val token = getTokenFromSharedPreferences()
        val userId = getIdFromSharedPreferences()

        if (token != null && userId != null) {
            val retrofit = getRetrofit()
            val service = retrofit.create(ProjectDetailRetrofitInterface::class.java)
            val bearerToken = "Bearer $token"

            service.postNewProject(bearerToken, projectRequestDto).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    when (response.code()) {
                        200 -> {
                            view.onProjectPostSuccess()
                            Log.d("ProjectPostDetail", "200 성공")
                        }

                        201 -> {
                            view.onProjectPostSuccess()
                            Log.d("ProjectPostDetail", "201 성공")
                        }

                        401 -> {
                            view.onProjectPostFailure(401, "Unauthorized")
                        }

                        403 -> {
                            view.onProjectPostFailure(403, "Forbidden")
                        }

                        404 -> {
                            view.onProjectPostFailure(404, "Not Found")
                        }

                        else -> {
                            view.onProjectPostFailure(response.code(), "기타 에러: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    view.onProjectPostFailure(-1, "네트워크 오류: ${t.message}")
                }
            })
        } else {
            Log.e("ProjectDetailPostService", "토큰 또는 사용자 ID를 찾을 수 없습니다.")
            view.onProjectPostFailure(-1, "토큰 또는 사용자 ID를 찾을 수 없습니다.")
        }
    }
}