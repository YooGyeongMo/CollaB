package com.gmlab.team_benew.main.notification

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class NotificationReadService {
    private lateinit var notificationReadView: NotificationReadView

    fun setNotificationReadView(notificationReadView: NotificationReadView){
        this.notificationReadView = notificationReadView
    }

    suspend fun alarmsRead(context: Context, alaramId: Long): Response<ResponseBody>{
        val token = getTokenFromSharedPreferences(context)
        val bearerToken = "Bearer $token"
        val userId = getIdFromSharedPreferences(context)?.toLong() ?: -1L

        val notificationService = getRetrofit().create(NotificationPutRetrofitInterface::class.java)
        return notificationService.alarmsPut(bearerToken, userId, alaramId).execute()
    }



//    Callback<ResponseBody>{
//        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//            Log.d("NETWORK_SUCCESS_ALARMS_READ","USER_MATCHING_READ_ALARMS_네트워크성공")
//            when(response.code()){
//                200 -> {
//                    notificationReadView.onNotificationReadSuccess()
//                }
//                401-> {
//                    Log.e("NotificationRead/401", "401 ERROR ${response.code()}")
//                    notificationReadView.onNotificationReadFailure()
//                }
//                else-> {
//                    Log.e("NotificationRead/ERROR", "ERROR ${response.code()}")
//                    notificationReadView.onNotificationReadFailure()
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            Log.e("NETWORK_FAILURE_ALARMS_READ","USER_MATCHING_READ_ALARMS_네트워크실패")
//            notificationReadView.onNotificationReadFailure()
//        }
//
//    })

    private fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }

    private fun getIdFromSharedPreferences(context: Context): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId",-1).takeIf {it != -1  }
    }

}