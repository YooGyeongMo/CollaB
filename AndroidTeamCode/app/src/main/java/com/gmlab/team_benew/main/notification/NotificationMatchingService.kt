package com.gmlab.team_benew.main.notification

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response

class NotificationMatchingService {

    private lateinit var notificationView: NotificationView

    fun setNotificationView(notificationView: NotificationView){
        this.notificationView = notificationView
    }

    fun getNotificationList(context: Context){
        val notificationService = getRetrofit().create(NotificationMatchingRetrofitInterface::class.java)
        val userId = getIdFromSharedPreferences(context)?.toLong() ?: -1L
        val token = getTokenFromSharedPreferences(context)
        val bearerToken = "Bearer $token"

        notificationService.alarmsGet(bearerToken, userId).enqueue(object: Callback<List<NotificationMatchingResponse>> {
            override fun onResponse(
                call: Call<List<NotificationMatchingResponse>>,
                response: Response<List<NotificationMatchingResponse>>
            ) {
                Log.d("NETWORK_MATCHING_GET_SUCCESS","USER_MATCHING_GET_네트워크성공")
                when(response.code()){
                    200 -> {
                        val notifications = response.body() ?: return
                        notificationView.onNotificationListSuccess(notifications)
                        notificationView.onNotificationSuccess()
                        }
                    401 -> {
                        notificationView.onNotificationFailure()
                    }
                    else -> {
                        Log.e("NOTIFICATION/RESPONSE/ERROR", "Error with response code: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<NotificationMatchingResponse>>, t: Throwable) {
                Log.d("NETWORK_MATCHING_PATCH_FAILURE","USER_MATCHING_GET_네트워크실패")
            }

        })

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