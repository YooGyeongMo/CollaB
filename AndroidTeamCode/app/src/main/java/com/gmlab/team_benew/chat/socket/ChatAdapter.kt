package com.gmlab.team_benew.chat.socket

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R

class ChatAdapter(private val messages: List<StompMessage>, private val currentUserId: Int) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>()  {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.tv_chat_message)
        val senderText: TextView = itemView.findViewById(R.id.tv_chat_sender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.message
        holder.senderText.text = message.senderName  // 발신자의 이름 설정

        // 현재 사용자의 ID와 메시지 송신자의 ID 비교
        // senderId를 사용해야 좌우 정렬이 됨
        val isCurrentUser = message.sender == currentUserId

        holder.messageText.gravity = if (isCurrentUser) Gravity.END else Gravity.START
        holder.senderText.gravity = if (isCurrentUser) Gravity.END else Gravity.START

    }

    override fun getItemCount(): Int {
        return messages.size
    }
}