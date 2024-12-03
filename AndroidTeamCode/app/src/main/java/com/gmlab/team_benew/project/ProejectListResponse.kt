package com.gmlab.team_benew.project

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectResponse(
    @SerializedName("creationDate") val creationDate: String,
    @SerializedName("numberOfMembers") val numberOfMembers: Int,
    @SerializedName("profiles") val profiles: List<Profile>,
    @SerializedName("projectDeadlineDate") val projectDeadlineDate: String,
    @SerializedName("projectId") val projectId: Long,
    @SerializedName("projectIntroduction") val projectIntroduction: String,
    @SerializedName("projectName") val projectName: String,
    @SerializedName("projectOneLineIntroduction") val projectOneLineIntroduction: String,
    @SerializedName("projectStartDate") val projectStartDate: String,
    @SerializedName("projectStarted") val projectStarted: Boolean
) : Parcelable

@Parcelize
data class Profile(
    @SerializedName("id") val id: Int,
    @SerializedName("instruction") val instruction: String,
    @SerializedName("member") val member: Member,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("peer") val peer: Int,
    @SerializedName("personalLink") val personalLink: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("projectExperience") val projectExperience: Boolean,
    @SerializedName("role") val role: String
) : Parcelable

@Parcelize
data class Member(
    @SerializedName("account") val account: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("id") val id: Int,
    @SerializedName("major") val major: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("roles") val roles: List<Role>,
    @SerializedName("token") val token: String
) : Parcelable

@Parcelize
data class Role(
    @SerializedName("name") val name: String
) : Parcelable
