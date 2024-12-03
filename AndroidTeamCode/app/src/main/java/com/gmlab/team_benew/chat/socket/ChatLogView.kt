package com.gmlab.team_benew.chat.socket

interface ChatLogView {

    //채팅 로그 들고오기 성공
    fun onGetChattingLogSuccess(chatLogs: MutableList<ChatMessage>)
    //채팅 로그 들고오기 실패
    fun onGetChattingLogFailure()

}