package com.gmlab.team_benew.main

import android.util.Log
import com.gmlab.team_benew.auth.getOkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources

class SSEService(private val listener: SSEListener, private val token: String) {

    private var eventSource: EventSource? = null

    fun startSSE(userId: Int) {
        val client = getOkHttpClient()
        val request = Request.Builder()
            .url("http://3.34.143.117:8080/alarms/stream/$userId")
            .header("Authorization", "Bearer $token")
            .build()
        Log.d("SSESERVICE","SSE통신완료")
        eventSource = EventSources.createFactory(client).newEventSource(request, object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                Log.d("SSE", "Connection opened")
                listener.onConnectionOpened()
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                Log.d("SSE", "New event: $data")
                listener.onNewEvent(data)
            }

            override fun onClosed(eventSource: EventSource) {
                Log.d("SSE", "Connection closed")
                listener.onConnectionClosed()
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                Log.e("SSE", "Connection failed", t)
                listener.onConnectionError(t)
            }
        })
    }

    fun stopSSE() {
        eventSource?.cancel()
    }

    interface SSEListener {
        fun onNewEvent(data: String)
        fun onConnectionError(t: Throwable?)
        fun onConnectionOpened()
        fun onConnectionClosed()
    }
}
