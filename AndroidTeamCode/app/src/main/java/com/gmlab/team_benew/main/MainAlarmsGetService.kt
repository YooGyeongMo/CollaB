package com.gmlab.team_benew.main

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainAlarmsGetService(private val context:MainActivity) {
    private lateinit var mainLiveAlarmsView: MainLiveAlarmsView


    fun setMainLiveAlarmsView(mainLiveAlarmsView: MainLiveAlarmsView){
        this.mainLiveAlarmsView = mainLiveAlarmsView
    }

    fun getUserAlarms(context: Context){
        val authService= getRetrofit().create(MainLiveGetAlarmsRetrofitInterface::class.java)
        val token = getTokenFromSharedPreferences(context)
        val bearerToken = "Bearer $token"
        val userId = getIdFromSharedPreferences(context) ?: return
        authService.liveAlarmGet(bearerToken, userId).enqueue(object: Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("NETWORK/SUCCESS/LONGPOLLING","MAIN ACTIVITY 네트워크 연결성공 알람데이터")
                when(response.code()){
                    200 -> {
                        mainLiveAlarmsView.onMainLiveAlarmsGetSuccess(response.body()!!)
                    }

                    401 -> {
                        Log.e("MAIN/LIVE/ALARMS/401/ERROR", "${response.code()} 에러 발생")
                    }

                    else -> {
                        Log.e("MAIN/LIVE/ALARMS/ERROR", "${response.code()} 에러 발생")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("NETWORK/FAILURE/LONGPOLLING","MAIN ACTIVITY 네트워크 연결실패 알람데이터")
            }

        }
        )
    }


    private fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }

    private fun getIdFromSharedPreferences(context: Context): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 }
    }

}