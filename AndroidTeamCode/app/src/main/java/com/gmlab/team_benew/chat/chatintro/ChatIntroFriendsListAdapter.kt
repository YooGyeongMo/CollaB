import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmlab.team_benew.R
import okio.ByteString.Companion.decodeBase64

class ChatIntroFriendsListAdapter(private val friendsList: MutableList<Friend>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_FRIEND = 1
    private val VIEW_TYPE_NO_FRIEND = 0

    override fun getItemViewType(position: Int): Int {
        return if (friendsList.isEmpty()) VIEW_TYPE_NO_FRIEND else VIEW_TYPE_FRIEND
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FRIEND) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_freinds_list_chat_fragment, parent, false)
            FriendViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_no_friends_list_chat_fragment, parent, false)
            NoFriendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FriendViewHolder) {
            val friend = friendsList[position]
            holder.userName.text = friend.name
            val photoUrl = friend.profileImageUrl

            if (photoUrl.isNullOrEmpty()) {
                // photo가 null이거나 빈 문자열인 경우
                Glide.with(holder.profileImage.context)
                    .load(R.drawable.male_avatar) // 기본 아바타 이미지
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.profileImage)
            } else {
                // photo가 Base64 문자열인 경우
                val bitmap = decodeBase64(photoUrl)
                if (bitmap != null) {
                    Glide.with(holder.profileImage.context)
                        .load(bitmap)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.profileImage)
                } else {
                    Glide.with(holder.profileImage.context)
                        .load(photoUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.profileImage)
                }
            }
        } else if (holder is NoFriendViewHolder) {
            holder.message.text = "친구 목록이 비었습니다."
        }
    }

    override fun getItemCount(): Int {
        return if (friendsList.isEmpty()) 1 else friendsList.size
    }

    fun updateFriendsList(newFriendsList: List<Friend>) {
        friendsList.clear()
        friendsList.addAll(newFriendsList)
        notifyDataSetChanged()
    }
    private fun decodeBase64(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.iv_profile_friends_pic)
        val userName: TextView = view.findViewById(R.id.tv_friends_list_item_user_name)
    }

    class NoFriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.findViewById(R.id.tv_no_friends_list_item_user_name)
    }
}

data class Friend(
    val name: String,
    val profileImageUrl: String, // URL로 변경
)

data class FriendResponse(
    val profile: Profile,
    val friendProfile: friendProfile,
    val status: String
)

data class friendProfile(
    val id: Long,
    val member: Member,
    val photo: String
)

data class Profile(
    val id: Long,
    val member: Member,
    val photo: String
)

data class Member(
    val name: String
)