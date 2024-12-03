package com.gmlab.team_benew.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.chat.retrofit.ChatListRoomGet_Interface
import com.gmlab.team_benew.chat.retrofit.ChatRoomListModelItem
import com.gmlab.team_benew.chat.retrofit.chatdata
import com.gmlab.team_benew.databinding.FragmentChatlistBinding
import com.gmlab.team_benew.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListFragment : Fragment(), ChatListView {
    private lateinit var chatAdapter: ChatlistAdapter
    private lateinit var chatListService: ChatListService

    private var binding: FragmentChatlistBinding? = null


    override fun onCreateView(//뷰가 처음 시작될 때, 일단 얘는 앱에서 실행됨
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding = FragmentChatlistBinding.inflate(inflater,container,false)
        //return binding?.root
        binding = FragmentChatlistBinding.inflate(inflater, container, false)
        return binding?.root

    }


    //onViewCreated는 onCreateView에 의해 완전히 생성된 뷰가 준비된 후 호출되므로, 이 메서드에서 뷰와 관련된 초기화 및 설정 작업을 수행ㅐㅜㅍ
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ChatlistAdapter 생성시 NavController에 전달
        chatAdapter = ChatlistAdapter(findNavController())

        //RecyclerView 설정
        binding?.myRecyclerViewChat?.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        chatListService = ChatListService(requireContext())
        chatListService.setChatListView(this)
        chatListService.getChatList()
    }


    fun updateUIChatlst(chatList: MutableList<chatdata>) {
        chatAdapter.modelList = chatList
        chatAdapter.notifyDataSetChanged()
    }


    override fun onChatListGetSuccess(chatRooms: MutableList<ChatRoomListModelItem>) {
        val chatDataList = chatRooms.map { chatdata(it.roomId, it.name) }.toMutableList()
        updateUIChatlst(chatDataList)
        Log.d("Chatting/UserList/GET", "유저 채팅방LIST 성공")
    }

    override fun onChatListGetFailure(errorMessage: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Error")
            setMessage(errorMessage)
            setPositiveButton("OK") { dialog, which ->
                // 오류 대화상자 확인 버튼 클릭 시 수행할 동작
            }
            create().show()
        }
    }


}