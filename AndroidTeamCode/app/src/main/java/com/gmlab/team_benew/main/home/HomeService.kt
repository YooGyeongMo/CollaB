package com.gmlab.team_benew.main.home

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.project.ProjectResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeService(private val context: Context) {
    private lateinit var homeView: HomeView

    fun setHomeView(homeView: HomeView){
        this.homeView = homeView
    }


    fun getUserProfilePreviewData() {
        val token = getTokenFromSharedPreferences()
        val memberId = getIdFromSharedPreferences(context)

        if (token != null && memberId != null) {
            val homeService = getRetrofit().create(HomeRetrofitInterface::class.java)
            val bearerToken = "Bearer $token"
            homeService.getProfile(bearerToken, memberId).enqueue(object :
                Callback<getProfilePreiviewData> {
                override fun onResponse(call: Call<getProfilePreiviewData>, response: Response<getProfilePreiviewData>) {
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                homeView.onHomeGetSuccess(it)
                            }
                        }
                        401 -> {
                            homeView.onHomeGetFailure()
                        }
                        else -> {
                            homeView.onHomeGetFailure()
                        }
                    }
                }

                override fun onFailure(call: Call<getProfilePreiviewData>, t: Throwable) {
                    homeView.onHomeGetFailure()
                }
            })
        } else {
            homeView.onHomeGetFailure()
        }
    }

    fun getMainProjectData() {
        val token = getTokenFromSharedPreferences()
        val userId = getIdFromSharedPreferences(context)

        if (token != null && userId != null) {
            val homeService = getRetrofit().create(HomeRetrofitInterface::class.java)
            val bearerToken = "Bearer $token"
            homeService.getMainProject(bearerToken, userId).enqueue(object :
                Callback<getMainProjectData> {
                override fun onResponse(call: Call<getMainProjectData>, response: Response<getMainProjectData>) {
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                homeView.onMainProjectGetSuccess(it)
                            }
                        }
                        401, 403, 404 -> {
                            homeView.onMainProjectGetFailure(response.code())
                        }
                        else -> {
                            homeView.onMainProjectGetFailure(response.code())
                        }
                    }
                }

                override fun onFailure(call: Call<getMainProjectData>, t: Throwable) {
                    Log.e("NETWORK/FAILURE", "Home main project 네트워크 연결실패: ${t.message}")
                    homeView.onMainProjectGetFailure(-1) // 추가된 실패 처리
                }
            })
        } else {
            Log.e("인증/FAILURE","토큰 및 인증된 사용자 아님")
        }
    }


    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }

    private fun getIdFromSharedPreferences(context: Context): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 }
    }
}