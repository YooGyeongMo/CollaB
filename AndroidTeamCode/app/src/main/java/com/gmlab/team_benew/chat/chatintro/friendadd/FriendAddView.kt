package com.gmlab.team_benew.chat.chatintro.friendadd

interface FriendAddView {
    fun onFriendAddSuccess()
    fun onFriendAddFailure(message: String)
    fun onUnauthorized()
    fun onForbidden()
    fun onNotFound()
}