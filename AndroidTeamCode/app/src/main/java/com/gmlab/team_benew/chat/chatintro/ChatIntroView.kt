package com.gmlab.team_benew.chat.chatintro

import ChatData
import FriendResponse

interface ChatIntroView {
    fun onGetChatRoomsSuccess(chatRooms: List<ChatData>)
    fun onGetChatRoomsFailure(message: String)
    fun onGetFriendsListSuccess(friends: List<FriendResponse>)
    fun onGetFriendsListFailure(message: String)
    fun onUnauthorized()
    fun onForbidden()
    fun onNotFound()
}
