package com.gmlab.team_benew.chat.socket

import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatLogService {

    private lateinit var chatLogView: ChatLogView

    fun setChatLogView(chatLogView: ChatLogView) {
        this.chatLogView = chatLogView
    }

    fun getChatLog(context: Context, sendDate: String, roomId: String) {
        val token = getTokenFromSharedPreferences(context)
        val bearerToken = "Bearer $token"

        val chatLogService = getRetrofit().create(ChatLogRetrofitInterface::class.java)
        chatLogService.getChatTodayLog(bearerToken, sendDate, roomId).enqueue(object :
            Callback<MutableList<onMessageData>> {
            override fun onResponse(
                call: Call<MutableList<onMessageData>>,
                response: Response<MutableList<onMessageData>>
            ) {
                Log.d("네트워크통신/성공/채팅로그가져오기", "네트워크통신 성공!")

                when (response.code()) {
                    200 -> {
                        val chatLogsData = response.body() ?: emptyList()
                        // onMessageData 객체를 ChatMessage 객체로 변환
                        val chatLogs = chatLogsData.map { data ->
                            ChatMessage(
                                sender = data.senderName,
                                message = data.message,
                                userId = data.sender,
                                senddate = data.senddate,
                                senderId = data.sender// senderId가 필요한 경우 적절한 값 설정
                            )
                        }.toMutableList()

                        chatLogView.onGetChattingLogSuccess(chatLogs)
                    }
                    401 -> {
                        Log.e("사용자 채팅 로그 불러오기/ERROR", " ERROR ${response.code()}")
                        chatLogView.onGetChattingLogFailure()
                    }

                    500 -> {
                        Log.e("사용자 채팅 로그 불러오기/ERROR", " ERROR ${response.code()}")
                        chatLogView.onGetChattingLogFailure()

                    }
                    else -> {
                        Log.e("사용자 채팅 로그 불러오기/ERROR", " ERROR ${response.code()}")
                        chatLogView.onGetChattingLogFailure()
                    }
                }

            }

            override fun onFailure(call: Call<MutableList<onMessageData>>, t: Throwable) {
                chatLogView.onGetChattingLogFailure()
            }
        })

    }

    private fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }

}