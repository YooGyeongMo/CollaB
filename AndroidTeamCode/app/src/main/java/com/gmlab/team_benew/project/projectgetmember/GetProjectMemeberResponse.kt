package com.gmlab.team_benew.project.projectgetmember

data class ProjectMemberResponse(
    val id: Int,
    val instruction: String,
    val member: Member,
    val nickname: String,
    val peer: Int,
    val personalLink: String,
    val photo: String,
    val projectExperience: Boolean,
    val promise: String,
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
