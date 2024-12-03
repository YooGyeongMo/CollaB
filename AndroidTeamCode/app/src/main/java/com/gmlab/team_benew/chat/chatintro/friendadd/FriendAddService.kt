package com.gmlab.team_benew.chat.chatintro.friendadd

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.chat.chatintro.friendalarm.FriendAlarmRequest
import com.gmlab.team_benew.chat.chatintro.friendalarm.FriendAlarmRetrofitInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriendAddService(private val context: Context, private val view:FriendAddView) {
    private val retrofit = getRetrofit()
    private val api = retrofit.create(FriendAddRetrofitInterface::class.java)
    private val alarmApi = retrofit.create(FriendAlarmRetrofitInterface::class.java)

    fun addFriend(friendId: Int, memberId: Int ) {
        val token = getTokenFromSharedPreferences()
        val name = cacheUserName()
        if (token.isNullOrEmpty()) {
            view.onFriendAddFailure("유효한 토큰을 찾을 수 없습니다.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val friendAddResponse = api.addFriend("Bearer $token", friendId.toLong(), memberId.toLong())
                if (friendAddResponse.isSuccessful) {
                    Log.d("FriendAddService", "친구 추가 요청 성공: ${friendAddResponse.body()}")
                    val alarmRequest = FriendAlarmRequest(
                        message = "${name}님이 친구 추가 요청을 보냈습니다",
                        receiverUserId = friendId.toLong(),
                        senderUserId = memberId.toLong()
                    )
                    val alarmResponse = alarmApi.createAlarm("Bearer $token", alarmRequest)
                    withContext(Dispatchers.Main) {
                        if (alarmResponse.isSuccessful) {
                            view.onFriendAddSuccess()
                        } else {

                            view.onFriendAddFailure("알람 생성 실패: ${alarmResponse.code()}")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        view.onFriendAddFailure("친구 추가 실패: ${friendAddResponse.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("FriendAddService", "네트워크 요청 실패", e)
                withContext(Dispatchers.Main) {
                    view.onFriendAddFailure("네트워크 요청 실패: ${e.message}")
                }
            }
        }
    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }

    private fun cacheUserName(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("cachedUserName", null)
    }
}