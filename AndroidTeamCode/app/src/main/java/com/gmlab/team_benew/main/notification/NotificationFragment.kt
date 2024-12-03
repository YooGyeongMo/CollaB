package com.gmlab.team_benew.main.notification

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.chat.chatintro.friendadd.FriendAddView
import com.gmlab.team_benew.main.notification.ProjectMemeberPatch.ProjectMemberPatchService
import com.gmlab.team_benew.main.notification.chattingpost.ChatUser
import com.gmlab.team_benew.main.notification.chattingpost.ChattingPostService
import com.gmlab.team_benew.main.notification.chattingpost.ChattingPostView
import com.gmlab.team_benew.main.notification.freindaccept.FriendAcceptService
import com.gmlab.team_benew.main.notification.matchingalarm.MatchingAlarmsPatchView
import com.gmlab.team_benew.main.notification.matchingalarm.MatchingPatchService
import com.gmlab.team_benew.main.notification.projectnotiget.GetProjectNotiResponse
import com.gmlab.team_benew.main.notification.projectnotiget.ProjectnotigetService
import com.gmlab.team_benew.main.notification.projectnotiget.ProjectnotigetView
import com.gmlab.team_benew.matching.MatchingAlarmResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class NotificationFragment : Fragment(), NotificationView, NotificationReadView, ChattingPostView,
    MatchingAlarmsPatchView, ProjectnotigetView,FriendAddView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationService: NotificationMatchingService
    private lateinit var notificationReadService: NotificationReadService
    private lateinit var matchingPatchService: MatchingPatchService
    private lateinit var chattingPostService: ChattingPostService
    private lateinit var projectMemberPatchService: ProjectMemberPatchService
    private lateinit var projectnotigetService: ProjectnotigetService
    private lateinit var friendAcceptService: FriendAcceptService

    // resquestLock 버튼 중복터치 막기
    private var requestLock = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // onViewCreated에서 뷰 요소들을 초기화합니다.
        recyclerView = view.findViewById(R.id.my_recycler_view_notificationList)
        recyclerView.layoutManager = LinearLayoutManager(context) // LayoutManager 설정
        // 알람 리스트 불러오는 로직
        notificationService = NotificationMatchingService()
        notificationService.setNotificationView(this)
        notificationService.getNotificationList(requireContext())
        // 알람 읽는 로직
        notificationReadService = NotificationReadService()
        notificationReadService.setNotificationReadView(this)

        // 알람 수락,거절 하는로직
        matchingPatchService = MatchingPatchService()
        matchingPatchService.setMatchingAlarmsPatchView(this)

        //최종 매칭 성립 조건 채팅방 만들기
        chattingPostService = ChattingPostService()
        chattingPostService.setChattingPostView(this)

        friendAcceptService = FriendAcceptService(requireContext(), this)


        projectMemberPatchService = ProjectMemberPatchService()

        projectnotigetService = ProjectnotigetService(requireContext())
        projectnotigetService.setProjectnotiView(this)
    }

    override fun onNotificationListSuccess(notifications: List<NotificationMatchingResponse>) {
        val adapter = NotificationAdapter(notifications.toMutableList(),
            onAcceptGeneral = { notification ->
                Log.d("수락", "수락")
                handleAcceptGeneral(notification)
            },
            onRejectGeneral = { notification ->
                Log.d("거절", "거절")
                handleRejectGeneral(notification)
            },
            onProjectPreview = { projectId ->
                projectnotigetService.getProjectNoti(projectId)
            },
            onAcceptFriend = { notification ->
                Log.d("친구 수락", "친구 수락")
                handleAcceptFriend(notification)
            },
            onRejectFriend = { notification ->
                Log.d("친구 거절", "친구 거절")
                handleRejectFriend(notification)
            }
        )
        recyclerView.adapter = adapter
    }


    private fun handleAcceptGeneral(notification: NotificationMatchingResponse) {
        if (requestLock) return
        requestLock = true

        CoroutineScope(Dispatchers.IO).launch {
            val senderId = notification.senderUserId
            val senderName = extractUserName(notification.message)
            val receiverId = getIdFromSharedPreferences(requireContext())?.toLong() ?: -1L
            val projectId = extractProjectId(notification.message)
            val receiverName = getCachedUserName() ?: "Unknown"

            val chatUsers = listOf(
                ChatUser(senderId, senderName),
                ChatUser(receiverId, receiverName)
            )

            try {
                val readResponse = notificationReadService.alarmsRead(requireContext(), notification.id)
                if (!readResponse.isSuccessful) {
                    handleFailure("알람 읽기에 실패했습니다. 오류 코드: ${readResponse.code()}")
                    return@launch
                }
                Log.d("NotificationFragment", "알람 읽기 성공: ${readResponse.code()}")

                val patchResponse = projectMemberPatchService.addProjectMember(
                    requireContext(),
                    projectId,
                    receiverId
                )
                if (!patchResponse.isSuccessful) {
                    handleFailure("프로젝트 팀원 추가에 실패했습니다. 오류 코드: ${patchResponse.code()}")
                    return@launch
                }
                Log.d("NotificationFragment", "프로젝트 팀원 추가 성공: ${patchResponse.code()}")

                val chatResponse = chattingPostService.chattingPost(requireContext(), chatUsers)
                if (chatResponse.isSuccessful) {
                    Log.d("NotificationFragment", "채팅방 생성 성공: ${chatResponse.code()}")
                    withContext(Dispatchers.Main) {
                        showSuccessDialog("해당 프로젝트에 팀원으로 추가되었습니다.\n해당 프로젝트 채팅방이 생성되었습니다.")
                        (recyclerView.adapter as? NotificationAdapter)?.removeItem(notification)
                    }
                } else {
                    handleFailure("채팅방 생성에 실패했습니다. 오류 코드: ${chatResponse.code()}")
                }
            } catch (e: Exception) {
                Log.e("NotificationFragment", "네트워크 요청에 실패했습니다.", e)
                handleFailure("네트워크 요청에 실패했습니다. ${e.message}")
            } finally {
                requestLock = false
            }
        }
    }

    private fun handleRejectGeneral(notification: NotificationMatchingResponse) {
        if (requestLock) return
        requestLock = true

        CoroutineScope(Dispatchers.IO).launch {
            val senderId = notification.senderUserId
            val receiverId = getIdFromSharedPreferences(requireContext())?.toLong() ?: -1L

            try {
                val readResponse = notificationReadService.alarmsRead(requireContext(), notification.id)
                if (readResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        (recyclerView.adapter as? NotificationAdapter)?.removeItem(notification)
                        showSuccessDialog("매칭 요청을 거절했습니다.")
                    }
//                    val matchResponse = matchingPatchService.rejectMatch(requireContext(), senderId, receiverId)
//                    if (matchResponse.isSuccessful) {
//                        withContext(Dispatchers.Main) {
//                            (recyclerView.adapter as? NotificationAdapter)?.removeItem(notification)
//                            showSuccessDialog("매칭 요청을 거절했습니다.")
//                        }
//                    } else {
//                        handleFailure("매칭 거절에 실패했습니다.")
//                    }
                } else {
                    handleFailure("알람 읽기에 실패했습니다.")
                }
            } catch (e: Exception) {
                handleFailure("네트워크 요청에 실패했습니다.")
            } finally {
                requestLock = false
            }
        }
    }

    private fun handleAcceptFriend(notification: NotificationMatchingResponse) {
        if (requestLock) return
        requestLock = true

        CoroutineScope(Dispatchers.IO).launch {
            val senderUserId = notification.senderUserId
            val receiverUserId = getIdFromSharedPreferences(requireContext())?.toLong() ?: -1L

            try {
                val readResponse = notificationReadService.alarmsRead(requireContext(), notification.id)
                if (!readResponse.isSuccessful) {
                    handleFailure("알람 읽기에 실패했습니다. 오류 코드: ${readResponse.code()}")
                    return@launch
                }
                Log.d("NotificationFragment", "알람 읽기 성공: ${readResponse.code()}")

                friendAcceptService.acceptFriend(senderUserId, receiverUserId)
                withContext(Dispatchers.Main) {
                    (recyclerView.adapter as? NotificationAdapter)?.removeItem(notification)
                }
            } catch (e: Exception) {
                Log.e("NotificationFragment", "네트워크 요청에 실패했습니다.", e)
                handleFailure("네트워크 요청에 실패했습니다. ${e.message}")
            } finally {
                requestLock = false
            }
        }
    }

    private fun handleRejectFriend(notification: NotificationMatchingResponse) {
        // 친구 거절에 대한 로직 구현
        if (requestLock) return
        requestLock = true

        CoroutineScope(Dispatchers.IO).launch {
            val senderId = notification.senderUserId
            val receiverId = getIdFromSharedPreferences(requireContext())?.toLong() ?: -1L

            try {
                val readResponse = notificationReadService.alarmsRead(requireContext(), notification.id)
                if (readResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        (recyclerView.adapter as? NotificationAdapter)?.removeItem(notification)
                        showSuccessDialog("친구 요청을 거절했습니다.")
                    }

                } else {
                    handleFailure("알람 읽기에 실패했습니다.")
                }
            } catch (e: Exception) {
                handleFailure("네트워크 요청에 실패했습니다.")
            } finally {
                requestLock = false
            }
        }
    }

    private fun showProjectDetailModal(response: GetProjectNotiResponse) {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.modal_project_noti_info,null)
        builder.setView(view)

        val dialog = builder.create()

        val projectName: TextView = view.findViewById(R.id.tv_project_noti_title_name)
        val projectKind: TextView = view.findViewById(R.id.tv_project_noti_kind_data)
        val projectMemberCount: TextView = view.findViewById(R.id.tv_project_noti_team_member_count_data)
        val projectSummary: TextView = view.findViewById(R.id.tv_project_noti_summary_introduction_info_data)
        val btn_cancel: Button = view.findViewById(R.id.btn_project_noti_cancel)

        projectName.text = response.projectName
        projectKind.text = "프로젝트 유형" // 여기에 실제 프로젝트 유형 데이터를 넣어야 합니다.
        projectMemberCount.text = "${response.numberOfMembers}명"
        projectSummary.text = response.projectOneLineIntroduction

        btn_cancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun handleFailure(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            showFailureDialog(message)
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("성공")
            setMessage(message)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun showFailureDialog(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("오류")
            setMessage(message)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun getIdFromSharedPreferences(context: Context): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 }
    }

    private fun getCachedUserName(): String? {
        val sharedPref = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("cachedUserName", null)
    }

    private fun extractUserName(message: String): String {
        val regex = Regex("(\\S+) 님이")
        val matchResult = regex.find(message)
        return matchResult?.groupValues?.get(1) ?: "Unknown"
    }


    private fun extractProjectId(message: String): Long {
        val regex = Regex("프로젝트.*번호\\s(\\d+)")
        val matchResult = regex.find(message)
        return matchResult?.groupValues?.get(1)?.toLong() ?: -1L
    }

    override fun onNotificationSuccess() {
        Log.d("NOTIFICATION/LIST/GET/SUCCESS", "유저에 대한 알람 리스트 성공")
    }

    override fun onNotificationFailure() {
        Log.e("NOTIFICATION/LIST/GET/FAILURE", "401 인증 에러 유저에 대한 알람 리스트 실패")
    }

    override fun onNotificationReadSuccess() {
        Log.d("NOTIFICATION/READ/PUT/SUCCESS", "유저에 대한 알람 읽기 성공")
    }

    override fun onNotificationReadFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            showFailureDialog("네트워크 요청이 실패했습니다.")
        }
    }

    override fun onMatchingAlarmsAccessSuccess() {
        Log.d("NOTIFICATION/USER/매칭/수락", "유저에 대한 매칭 수락 성공")
    }

    override fun onMatchingAlarmsAccessFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            showFailureDialog("매칭 수락 네트워크 오류")
        }
    }

    override fun onMatchingAlarmsRejectSuccess() {
        Log.d("NOTIFICATION/USER/매칭/거절", "유저에 대한 매칭 거절 성공")
    }

    override fun onMatchingAlarmsRejectFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            showFailureDialog("매칭 거절 네트워크 오류")
        }
    }

    override fun onChattingPostSuccess() {
        Log.d("CHATTING/POST/성공", "유저에 대한 채팅방 만들기 성공")
        showSuccessDialog("최종 매칭 성공!")
    }

    override fun onChattingPostFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            showFailureDialog("최종 매칭 네트워크 오류")
        }
    }

    override fun onProjectNotiSuccess(response: GetProjectNotiResponse) {
        showProjectDetailModal(response)
    }

    override fun onProjectNotiFailure(statusCode: Int) {
        showFailureDialog("프로젝트 정보를 불러오는데 실패했습니다. 오류 코드: $statusCode")
    }

    override fun onFriendAddSuccess() {
        showSuccessDialog("친구 요청을 수락했습니다.")
    }

    override fun onFriendAddFailure(message: String) {
        showFailureDialog(message)
    }

    override fun onUnauthorized() {
        showFailureDialog("Unauthorized: Please login again.")
    }

    override fun onForbidden() {
        showFailureDialog("Forbidden: You do not have permission to access this resource.")
    }

    override fun onNotFound() {
        showFailureDialog("Not Found: The requested resource could not be found.")
    }
}
