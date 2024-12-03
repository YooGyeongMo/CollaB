package com.gmlab.team_benew.chat.socket

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import io.reactivex.Completable
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader


class StompClientManager(var token: String) {

    private lateinit var stompClient: StompClient
    private val gson = Gson()
    private var isConnected = false

    fun connect() {
        val headers = listOf(StompHeader("Authorization", "Bearer $token"))

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://3.34.143.117:8080/stompChat/websocket")
        stompClient.connect(headers) // 수정된 부분

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("Stomp", "Stomp connection opened")
                    isConnected = true
                }
                LifecycleEvent.Type.ERROR -> {
                    Log.e("Stomp", "Stomp connection error", lifecycleEvent.exception)
                    isConnected = false
                    reconnectWithDelay()
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.d("Stomp", "Stomp connection closed")
                    isConnected = false
                    reconnectWithDelay()
                }
                else -> Log.d("Stomp", "Unknown Stomp lifecycle event")
            }
        }
    }

    private fun reconnectWithDelay() {
        // 일정 시간 간격으로 재연결 시도
        Handler(Looper.getMainLooper()).postDelayed({
            connect()
        }, 5000) // 5초 후 재연결 시도
    }

    fun subscribe(roomId: String, onMessageReceived: (StompMessage) -> Unit) {
        val topic = "/sub/message/$roomId"
        stompClient.topic(topic).subscribe({ stompMessage ->
            val receiveMessage = parseMessage(stompMessage.payload)
            onMessageReceived(receiveMessage)
        }, { error ->
            Log.e("Stomp", "Subscription error", error)
        })
    }

    fun send(roomId: String, stompMessage: StompMessage): Completable {
        return Completable.create { emitter ->
            if (isConnected) {
                val payload = gson.toJson(stompMessage)
                val destination = "/pub/message/$roomId"
                stompClient.send(destination, payload).subscribe({
                    emitter.onComplete()
                }, { error ->
                    emitter.onError(error)
                })
            } else {
                emitter.onError(IllegalStateException("Not connected"))
            }
        }
    }


    fun disconnect() {
        if (isConnected) {
            stompClient.disconnect()
        }
    }

    private fun parseMessage(payload: String): StompMessage {
        return gson.fromJson(payload, StompMessage::class.java)
    }
}
