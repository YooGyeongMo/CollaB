package com.gmlab.team_benew.project.projectgetdetail

import java.util.Date

data class GetProjectDeatilResponse(
    val creationDate: Date,
    val numberOfMembers: Int,
    val profiles: List<Profile>,
    val projectDeadlineDate: String,
    val projectId: Int,
    val projectIntroduction: String,
    val projectName: String,
    val projectOneLineIntroduction: String,
    val projectStartDate: String,
    val projectStarted: Boolean
)

data class Profile(
    val id: Int,
    val instruction: String,
    val member: Member,
    val nickname: String,
    val peer: Int,
    val personalLink: String,
    val projectExperience: Boolean,
    val role: String
)

data class Member(
    val account: String,
    val birthday: String,
    val email: String,
    val gender: String,
    val id: Int,
    val major: String,
    val name: String,
    val password: String,
    val phoneNumber: String,
    val roles: List<Role>,
    val token: String
)

data class Role(
    val name: String
)