package com.gmlab.team_benew.chat.socket

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.databinding.FragmentChatlistBinding
import com.google.gson.Gson
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class ChatFragment : Fragment() {
    private lateinit var btn_send: Button
    private lateinit var et_sendMsg: EditText
    private lateinit var chatView: RecyclerView
    private lateinit var tv_roomName : TextView

    private lateinit var adapter: ChatAdapter
    private lateinit var roomId: String
    private lateinit var stompClientManager: StompClientManager

    // 채팅 리스트
    private val chatList = ArrayList<StompMessage>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val memberId = sharedPref?.getInt("loginId", 0) ?: 0  // 현재 사용자의 ID

        // 어댑터 초기화
        adapter = ChatAdapter(chatList, memberId)

        // RecyclerView 설정
        chatView.layoutManager = LinearLayoutManager(requireContext())
        chatView.adapter = adapter

        // roomId는 번들에서 받아온 값을 사용
        roomId = arguments?.getString("roomId") ?: ""
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chatting, container, false)

        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        val token = sharedPref?.getString("userToken", "")
        val name = sharedPref?.getString("cachedUserName", "")
        val memberId = sharedPref?.getInt("loginId", 0) ?: 0

        val getRoomId = arguments?.getString("roomId").toString()
        val getRoomName = arguments?.getString("roomName").toString()

        //룸id, senderName 설정
        roomId = getRoomId
        val senderName : String? = name

        btn_send = view.findViewById(R.id.btn_chatting_send)
        et_sendMsg = view.findViewById(R.id.et_chatting_sendMsg)
        chatView = view.findViewById(R.id.Recycler_chatting_chat)
        tv_roomName = view.findViewById(R.id.tv_chattingFragment_roomName)

        tv_roomName.text = getRoomName

        //채팅 히스토리 지금은 막아놨음
        //chatHistory()

        // StompClientManager 초기화
        stompClientManager = StompClientManager(token.toString())

        // STOMP 연결
        stompClientManager.connect()

        // 메시지 구독
        stompClientManager.subscribe(roomId) { stompMessage ->
            activity?.runOnUiThread {
                // 수신한 메시지를 chatList에 추가하고 RecyclerView를 갱신
                chatList.add(stompMessage)
                adapter.notifyDataSetChanged()
                chatView.scrollToPosition(chatList.size - 1)
            }
        }

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        //메세지 전송
        btn_send.setOnClickListener {
            val message = et_sendMsg.text.toString()
            if (message.isNotBlank()) {
                // 현재 날짜 및 시간을 LocalDateTime 객체로 가져옴
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                val sendDate = currentDateTime.format(formatter)

                val sendMessage = StompMessage(
                    message = message,
                    roomId = roomId,
                    sendDate = sendDate,
                    sender = memberId,
                    senderName = senderName!!
                )

                // 메시지 보내기
                stompClientManager.send(roomId, sendMessage)
                    .subscribe(object : CompletableObserver {
                        override fun onComplete() {
                            // 메시지 전송 성공 시 UI 피드백
                            activity?.runOnUiThread {
//                                Toast.makeText(requireContext(), "Message sent successfully", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onError(e: Throwable) {
                            // 메시지 전송 실패 시 UI 피드백
                            activity?.runOnUiThread {
//                                Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onSubscribe(d: Disposable) {
                            // 필요에 따라 추가 작업 수행 가능
                        }
                    })
                et_sendMsg.text.clear()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // STOMP 연결 해제
        stompClientManager.disconnect()
    }

    private fun chatHistory(){
        // 현재 날짜 및 시간을 LocalDateTime 객체로 가져옴
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val sendDate = currentDateTime.format(formatter)

        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPref?.getString("userToken", "")

        if (token != null) {
            val authService = getRetrofit().create(getChatHistoryRequest::class.java)
            authService.getMessages("Bearer $token", roomId, sendDate).enqueue(object :
                Callback<List<getChatHistoryData>> {
                override fun onResponse(
                    call: Call<List<getChatHistoryData>>,
                    response: Response<List<getChatHistoryData>>
                ) {
                    if (response.isSuccessful) {
                        val chatHistory = response.body()

                        // 채팅 기록을 처리하는 코드 작성
                        chatHistory?.let {
                            val sortedHistory = it.sortedBy { messageData -> messageData.sequence }
                            chatList.addAll(sortedHistory.map { messageData ->
                                StompMessage(
                                    message = messageData.message,
                                    roomId = messageData.roomId,
                                    sendDate = messageData.sendDate,
                                    sender = messageData.sender,
                                    senderName = messageData.senderName
                                )
                            })
                            adapter.notifyDataSetChanged()
                            chatView.scrollToPosition(chatList.size - 1)
                        }


                    } else {
                        // 오류 처리 코드 작성
                        Log.e("ChatFragment", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<getChatHistoryData>>, t: Throwable) {
                    // 실패 시 처리 코드 작성
                }
            })
        }
    }
}