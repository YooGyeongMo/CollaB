package com.gmlab.team_benew.main.notification.freindaccept

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.chat.chatintro.friendadd.FriendAddView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendAcceptService(private val context: Context, private val view: FriendAddView) {
    private val retrofit = getRetrofit()
    private val api = retrofit.create(FriendAcceptRetroInterface::class.java)

    fun acceptFriend(friendId: Long, memberId: Long) {
        val token = getTokenFromSharedPreferences()
        if (token.isNullOrEmpty()) {
            view.onFriendAddFailure("유효한 토큰을 찾을 수 없습니다.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.acceptFriend("Bearer $token", friendId, memberId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        view.onFriendAddSuccess()
                    } else {
                        view.onFriendAddFailure("친구 수락 실패: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
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
}