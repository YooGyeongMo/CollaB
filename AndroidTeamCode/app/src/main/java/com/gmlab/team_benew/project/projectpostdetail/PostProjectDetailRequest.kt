package com.gmlab.team_benew.project.projectpostdetail

data class PostProjectDetailRequest(
    val chatroomId: String?,
    val projectIntroduction: String,
    val projectName: String,
    val projectManager: Long,
    val projectOneLineIntroduction: String,
    val userId: Int
)