package com.gmlab.team_benew.main.notification

interface NotificationView {
    //알림성공
    fun onNotificationSuccess()
    // 알림 실패
    fun onNotificationFailure()
    // 알림 목록을 성공적으로 가져온 경우
    fun onNotificationListSuccess(notifications: List<NotificationMatchingResponse>)
}