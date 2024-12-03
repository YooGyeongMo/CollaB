import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import org.w3c.dom.Text

class ChatIntroChatListAdapter(
    private val context: Context,
    private val chatIntroChatList: MutableList<ChatData>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onAddFriendClick(chatData: ChatData)
        fun onChatRoomClick(chatData: ChatData)
    }

    private val VIEW_TYPE_CHAT_ROOM = 1
    private val VIEW_TYPE_NO_CHAT_ROOM = 0

    override fun getItemViewType(position: Int): Int {
        return if (chatIntroChatList.isEmpty() ||
            chatIntroChatList[position].roomId.isEmpty()
        ) VIEW_TYPE_NO_CHAT_ROOM else VIEW_TYPE_CHAT_ROOM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CHAT_ROOM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_list_chat_fragment, parent, false)
            ChatDataViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_no_chat_list_chat_fragment, parent, false)
            NoChatRoomViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatDataViewHolder) {
            val chatData = chatIntroChatList[position]
            holder.roomName.text = chatData.roomName

            holder.itemView.setOnClickListener {
                listener.onChatRoomClick(chatData)
            }

            holder.moreOptions.setOnClickListener {
                showPopupMenu(context, it, chatData)
            }

            holder.addFriend.setOnClickListener{
                listener.onAddFriendClick(chatData)
            }
        } else if (holder is NoChatRoomViewHolder) {
            holder.message.text = "채팅방이 없습니다. 새 채팅방을 만드세요"
        }
    }

    override fun getItemCount(): Int {
        return if (chatIntroChatList.isEmpty()) 1 else chatIntroChatList.size
    }

    private fun showPopupMenu(context: Context, anchorView: View, chatData: ChatData){
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_chat_list_chat_name_edit_or_disband, null)

        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true)
        popupWindow.elevation = 10f

        val actionChatListOut = popupView.findViewById<TextView>(R.id.action_chat_list_out)
        val actionChatListEdit = popupView.findViewById<TextView>(R.id.action_chat_list_edit)

        actionChatListOut.setOnClickListener {
                popupWindow.dismiss() // 사라지게하기 window
                // 채팅방 나가기 함수 작동 service작동할거임
        }

        actionChatListEdit.setOnClickListener {
            popupWindow.dismiss()
            //채팅방 이름 바꾸기 함수작동
        }


        //팝업 보이게하기
        popupWindow.showAsDropDown(anchorView,0,0)// 앵거 하는 뷰를 넣어서 x,y값주는 것임

    }


    fun updateChatList(newChatList: List<ChatData>) {
        chatIntroChatList.clear()
        chatIntroChatList.addAll(newChatList)
        notifyDataSetChanged()
    }

    class ChatDataViewHolder(
        view: View,

    ) : RecyclerView.ViewHolder(view) {
        val roomName: TextView = view.findViewById(R.id.tv_chat_list_item_user_name)
        val moreOptions: ImageView = view.findViewById(R.id.popup_chat_list_out)
        val addFriend: ImageView = view.findViewById(R.id.btn_add_freinds_this_chat_room)

    }

    class NoChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.findViewById(R.id.tv_no_chat_list_item_notice)
    }
}

data class ChatData(
    val projectId: Int,
    val roomId: String,
    val roomName: String
)
