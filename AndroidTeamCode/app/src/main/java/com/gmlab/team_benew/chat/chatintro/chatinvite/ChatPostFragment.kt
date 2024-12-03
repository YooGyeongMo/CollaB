package com.gmlab.team_benew.chat.chatintro.chatinvite

import ChatIntroFriendsListAdapter
import Friend
import FriendResponse
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.chat.chatintro.ChatIntroService
import com.gmlab.team_benew.chat.chatintro.ChatIntroViewModel


class ChatPostFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var friendsAdapter: ChatIntroFriendsListAdapter
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rc_chat_post_friend_list)
        progressBar = view.findViewById(R.id.chat_post_fragment_loading_indicator)

        friendsAdapter = ChatIntroFriendsListAdapter(mutableListOf())

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = friendsAdapter

        // ViewModel에서 친구 목록을 가져오는 로직
        val userId = getUserIdFromSharedPreferences()
        getFriendsList(userId.toLong())
    }

    private fun getFriendsList(memberId: Long) {
        val service = ChatIntroService(requireContext())
        progressBar.visibility = View.VISIBLE
        service.getFriendsList(memberId, object : ChatIntroService.ServiceCallback<List<FriendResponse>> {
            override fun onSuccess(data: List<FriendResponse>) {
                val myUserId = getUserIdFromSharedPreferences().toLong()
                val nonNullFriends = data.map {
                    if (it.profile.id == myUserId) {
                        Friend(
                            name = it.friendProfile.member.name,
                            profileImageUrl = it.friendProfile.photo // URL을 사용하여 이미지를 로드
                        )
                    } else {
                        Friend(
                            name = it.profile.member.name,
                            profileImageUrl = it.profile.photo
                        )
                    }
                }
                friendsAdapter.updateFriendsList(nonNullFriends)
                progressBar.visibility = View.GONE
            }

            override fun onFailure(message: String, code: Int) {
                showFailureDialog(message)
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun showFailureDialog(message: String) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext()).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun getUserIdFromSharedPreferences(): Int {
        val sharedPref = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 } ?: 0
    }
}