package com.gmlab.team_benew.chat

import com.gmlab.team_benew.chat.retrofit.ChatRoomListModelItem

interface ChatListView {
    //Chat List 불러오기 성공
    fun onChatListGetSuccess(chatRooms: MutableList<ChatRoomListModelItem>)
    //Chat List 불러오기 실패
    fun onChatListGetFailure(errorMessage: String)
}