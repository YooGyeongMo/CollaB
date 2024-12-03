package com.gmlab.team_benew.main.notification.projectnotiget


data class GetProjectNotiResponse(
    val numberOfMembers: Int,
    val projectName: String,
    val projectOneLineIntroduction: String
)