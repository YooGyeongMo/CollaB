package com.gmlab.team_benew.chat

import android.content.Context
import android.util.Log
import androidx.media3.common.C
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.chat.retrofit.ChatListRoomGet_Interface
import com.gmlab.team_benew.chat.retrofit.ChatRoomListModelItem
import com.gmlab.team_benew.matching.MatchingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListService(private  val context: Context) {
    private lateinit var chatListView: ChatListView

    fun setChatListView(chatListView: ChatListView){
        this.chatListView = chatListView
    }

    fun getChatList(){
        val token = getCurrentToken(context) ?: return
        val bearerToken = "Bearer $token"
        val userMyId = getIdFromSharedPreferences(context) ?: return

        val chatListService = getRetrofit().create(ChatListRoomGet_Interface::class.java)
        chatListService.getChatRoomList(bearerToken, userMyId).enqueue(object:
            Callback<MutableList<ChatRoomListModelItem>>
        {
            override fun onResponse(
                call: Call<MutableList<ChatRoomListModelItem>>,
                response: Response<MutableList<ChatRoomListModelItem>>
            ) {
                Log.d("NETWORK/SUCCESS/CHATLIST/GET","통신 성공")
                when(response.code()){
                    200->{
                        chatListView.onChatListGetSuccess(response.body() ?: mutableListOf())
                        Log.d("NETWORK/SUCCESS/CHATLIST/GET","통신 성공")
                    }

                    401->{
                        chatListView.onChatListGetFailure("401 인증되지 않은 사용자 에러.")
                    }
                    500->{
                        chatListView.onChatListGetFailure("500 Server 에러")
                    }
                    else -> {
                        chatListView.onChatListGetFailure("UnKnown Error")
                    }
                }

            }

            override fun onFailure(call: Call<MutableList<ChatRoomListModelItem>>, t: Throwable) {
                chatListView.onChatListGetFailure("유저 채팅방 리스트 받아오는 중 네트워크 오류 발생")
            }

        })
    }

    private fun getCurrentToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }
    private fun getIdFromSharedPreferences(context: Context): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 }
    }
}