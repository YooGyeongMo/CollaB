package com.gmlab.team_benew.main.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.main.notification.projectnotiget.GetProjectNotiResponse

class NotificationAdapter(
    private var items: MutableList<NotificationMatchingResponse>,
    private val onAcceptGeneral: (NotificationMatchingResponse) -> Unit,
    private val onRejectGeneral: (NotificationMatchingResponse) -> Unit,
    private val onProjectPreview: (Int) -> Unit,
    private val onAcceptFriend: (NotificationMatchingResponse) -> Unit,
    private val onRejectFriend: (NotificationMatchingResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_GENERAL = 0
    private val VIEW_TYPE_FRIEND = 1

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return if (item.message.contains("친구 추가 요청")) {
            VIEW_TYPE_FRIEND
        } else {
            VIEW_TYPE_GENERAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FRIEND) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification_friend, parent, false)
            FriendViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
            GeneralViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is FriendViewHolder) {
            holder.bind(item, onAcceptFriend, onRejectFriend)
        } else if (holder is GeneralViewHolder) {
            holder.bind(item, onAcceptGeneral, onRejectGeneral, onProjectPreview)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class GeneralViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val message: TextView = itemView.findViewById(R.id.tv_notification_message)
        private val acceptButton: TextView = itemView.findViewById(R.id.tv_notification_access)
        private val rejectButton: TextView = itemView.findViewById(R.id.tv_notification_reject)
        private val projectPreviewButton: Button = itemView.findViewById(R.id.btn_project_preview)

        fun bind(
            notificationMatchingResponse: NotificationMatchingResponse,
            onAccept: (NotificationMatchingResponse) -> Unit,
            onReject: (NotificationMatchingResponse) -> Unit,
            onProjectPreview: (Int) -> Unit
        ) {
            message.text = notificationMatchingResponse.message

            acceptButton.setOnClickListener { onAccept(notificationMatchingResponse) }
            rejectButton.setOnClickListener { onReject(notificationMatchingResponse) }
            val projectId = extractProjectId(notificationMatchingResponse.message)
            projectPreviewButton.setOnClickListener { onProjectPreview(projectId.toInt()) }
        }

        private fun extractProjectId(message: String): Int {
            val regex = Regex("프로젝트.*번호\\s(\\d+)")
            val matchResult = regex.find(message)
            return matchResult?.groupValues?.get(1)?.toInt() ?: -1
        }
    }

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val message: TextView = itemView.findViewById(R.id.tv_notification_message_friend)
        private val acceptButton: Button = itemView.findViewById(R.id.tv_notification_access_friend)
        private val rejectButton: Button = itemView.findViewById(R.id.tv_notification_reject_friend)

        fun bind(
            notificationMatchingResponse: NotificationMatchingResponse,
            onAccept: (NotificationMatchingResponse) -> Unit,
            onReject: (NotificationMatchingResponse) -> Unit
        ) {
            message.text = notificationMatchingResponse.message

            acceptButton.setOnClickListener { onAccept(notificationMatchingResponse) }
            rejectButton.setOnClickListener { onReject(notificationMatchingResponse) }
        }
    }

    fun removeItem(notification: NotificationMatchingResponse) {
        val position = items.indexOf(notification)
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}