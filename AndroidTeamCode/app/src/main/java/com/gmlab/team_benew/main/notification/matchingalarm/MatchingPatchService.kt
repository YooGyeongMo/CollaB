package com.gmlab.team_benew.main.notification.matchingalarm

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.matching.MatchingAlarmResponse
import retrofit2.Response

class MatchingPatchService {

    private lateinit var matchingAlarmsPatchView: MatchingAlarmsPatchView

    fun setMatchingAlarmsPatchView(matchingAlarmsPatchView: MatchingAlarmsPatchView) {
        this.matchingAlarmsPatchView = matchingAlarmsPatchView
    }

    suspend fun acceptMatch(
        context: Context,
        senderId: Long,
        receiverId: Long
    ): Response<MatchingAlarmResponse> {
        val token = getTokenFromSharedPreferences(context)
        val bearerToken = "Bearer $token"
        val service = getRetrofit().create(MatchingAlarmsReadRetrofitInterface::class.java)
        return service.matchingAlarmsAccess(bearerToken, senderId, receiverId).execute()
        //비동기 로직
        //        service.matchingAlarmsAccess(bearerToken, senderId, receiverId).enqueue(object: Callback<MatchingAlarmResponse> {
//            override fun onResponse(call: Call<MatchingAlarmResponse>, response: Response<MatchingAlarmResponse>) {
//                Log.d("NETWORK_SUCCESS_ALARMS_PATCH_수락","USER_MATCHING_PATCH_수락_네트워크성공")
//                when(response.code()){
//                    200 -> {
//                        matchingAlarmsPatchView.onMatchingAlarmsAccessSuccess()
//                    }
//                    401 -> {
//                        Log.e("NotificationPATCH/수락/ERROR", "401 ERROR ${response.code()}")
//                        matchingAlarmsPatchView.onMatchingAlarmsAccessFailure()
//                    }
//                    else -> {
//                        Log.e("NotificationPATCH/수락/ERROR", "ERROR ${response.code()}")
//                        matchingAlarmsPatchView.onMatchingAlarmsAccessFailure()
//                    }
//                }
//            }
//            override fun onFailure(call: Call<MatchingAlarmResponse>, t: Throwable) {
//                Log.e("NETWORK_SUCCESS_ALARMS_PATCH_수락","USER_MATCHING_PATCH_수락_네트워크성공",t)
//                matchingAlarmsPatchView.onMatchingAlarmsAccessFailure()
//            }
//        })
    }

    suspend fun rejectMatch(context: Context, senderId: Long, receiverId: Long): Response<MatchingAlarmResponse> {
        val token = getTokenFromSharedPreferences(context)
        val bearerToken = "Bearer $token"
        val service = getRetrofit().create(MatchingAlarmsReadRetrofitInterface::class.java)
//동기로직
        return service.matchingAlarmsReject(bearerToken, senderId, receiverId).execute()
        //비동기 로직
        //        service.matchingAlarmsReject(bearerToken, senderId, receiverId).enqueue(object:
//            Callback<MatchingAlarmResponse> {
//            override fun onResponse(call: Call<MatchingAlarmResponse>, response: Response<MatchingAlarmResponse>) {
//                Log.d("NETWORK_SUCCESS_ALARMS_PATCH_거절","USER_MATCHING_PATCH_거절_네트워크성공")
//                when(response.code()){
//                    200 -> {
//                        matchingAlarmsPatchView.onMatchingAlarmsRejectSuccess()
//                    }
//
//                    401 -> {
//                        Log.e("NotificationPATCH/거절/ERROR", "401 ERROR ${response.code()}")
//                        matchingAlarmsPatchView.onMatchingAlarmsRejectFailure()
//                    }
//
//                    else -> {
//                        Log.e("NotificationPATCH/거절/ERROR", "ERROR ${response.code()}")
//                        matchingAlarmsPatchView.onMatchingAlarmsRejectFailure()
//                    }
//                }
//            }
//            override fun onFailure(call: Call<MatchingAlarmResponse>, t: Throwable) {
//                Log.d("NETWORK_SUCCESS_ALARMS_PATCH_거절","USER_MATCHING_PATCH_거절_네트워크실패")
//                matchingAlarmsPatchView.onMatchingAlarmsRejectFailure()
//            }
//        })
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