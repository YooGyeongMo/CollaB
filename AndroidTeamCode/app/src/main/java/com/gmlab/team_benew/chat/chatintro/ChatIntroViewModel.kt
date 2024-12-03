package com.gmlab.team_benew.chat.chatintro

import ChatData
import FriendResponse
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ChatIntroViewModel(application: Application) : AndroidViewModel(application) {

    private val chatIntroService: ChatIntroService = ChatIntroService(application)
    private var chatIntroView: ChatIntroView? = null

    private val _friendsList = MutableLiveData<List<FriendResponse>>()
    val friendsList: LiveData<List<FriendResponse>> get() = _friendsList

    private val _chatRooms = MutableLiveData<List<ChatData>>()
    val chatRooms: LiveData<List<ChatData>> get() = _chatRooms

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun setView(view: ChatIntroView) {
        chatIntroView = view
    }

    fun getFriendsList(memberId: Long) {
        Log.d("ChatIntroViewModel", "getFriendsList called")
        chatIntroService.getFriendsList(memberId, object : ChatIntroService.ServiceCallback<List<FriendResponse>> {
            override fun onSuccess(data: List<FriendResponse>) {
                Log.d("ChatIntroViewModel", "getFriendsList onSuccess")
                chatIntroView?.onGetFriendsListSuccess(data)
            }

            override fun onFailure(message: String, code: Int) {
                Log.e("ChatIntroViewModel", "getFriendsList onFailure: $message, code: $code")
                handleFailure(message, code)
            }
        })
    }

    fun getChatRooms(userId: Int) {
        Log.d("ChatIntroViewModel", "getChatRooms called")
        chatIntroService.getChatRooms(userId, object : ChatIntroService.ServiceCallback<List<ChatData>> {
            override fun onSuccess(data: List<ChatData>) {
                Log.d("ChatIntroViewModel", "getChatRooms onSuccess")
                chatIntroView?.onGetChatRoomsSuccess(data)
            }

            override fun onFailure(message: String, code: Int) {
                Log.e("ChatIntroViewModel", "getChatRooms onFailure: $message, code: $code")
                handleFailure(message, code)
            }
        })
    }

    private fun handleFailure(message: String, code: Int) {
        when (code) {
            401 -> chatIntroView?.onUnauthorized()
            403 -> chatIntroView?.onForbidden()
            404 -> chatIntroView?.onNotFound()
            else -> {
                chatIntroView?.onGetChatRoomsFailure(message)
                chatIntroView?.onGetFriendsListFailure(message)
            }
        }
    }
}
