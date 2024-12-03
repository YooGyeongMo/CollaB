package com.gmlab.team_benew.chat.chatintro

import ChatData
import FriendResponse
import android.content.Context
import android.util.Log
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatIntroService(private val context: Context) {

    interface ServiceCallback<T> {
        fun onSuccess(data: T)
        fun onFailure(message: String, code: Int)
    }

    fun getChatRooms(userId: Int, callback: ServiceCallback<List<ChatData>>) {
        val token = getTokenFromSharedPreferences()
        if (token != null) {
            val bearerToken = "Bearer $token"
            val service = getRetrofit().create(ChatIntroRetrofitInterface::class.java)
            service.getChatRooms(bearerToken, userId).enqueue(object : Callback<List<ChatData>> {
                override fun onResponse(call: Call<List<ChatData>>, response: Response<List<ChatData>>) {
                    Log.d("ChatIntroService", "getChatRooms onResponse: ${response.code()}")
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body() ?: listOf())
                    } else {
                        callback.onFailure("Error: ${response.code()}", response.code())
                    }
                }

                override fun onFailure(call: Call<List<ChatData>>, t: Throwable) {
                    Log.e("ChatIntroService", "getChatRooms onFailure: ${t.message}")
                    callback.onFailure(t.message ?: "Network error", 500)
                }
            })
        } else {
            callback.onFailure("Token not found", 401)
        }
    }

    fun getFriendsList(memberId: Long, callback: ServiceCallback<List<FriendResponse>>) {
        val token = getTokenFromSharedPreferences()
        if (token != null) {
            val bearerToken = "Bearer $token"
            val service = getRetrofit().create(ChatIntroRetrofitInterface::class.java)
            service.getFriendsList(bearerToken, memberId).enqueue(object : Callback<List<FriendResponse>> {
                override fun onResponse(call: Call<List<FriendResponse>>, response: Response<List<FriendResponse>>) {
                    Log.d("ChatIntroService", "getFriendsList onResponse: ${response.code()}")
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body() ?: listOf())
                    } else {
                        callback.onFailure("Error: ${response.code()}", response.code())
                    }
                }

                override fun onFailure(call: Call<List<FriendResponse>>, t: Throwable) {
                    Log.e("ChatIntroService", "getFriendsList onFailure: ${t.message}")
                    callback.onFailure(t.message ?: "Network error", 500)
                }
            })
        } else {
            callback.onFailure("Token not found", 401)
        }
    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }
}
