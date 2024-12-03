package com.gmlab.team_benew.profile

import com.google.gson.annotations.SerializedName

data class getProfileDetailData(
    @SerializedName("instruction")
    val instruction: String,
    @SerializedName("member")
    val member: ProfileMember,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("personalLink")
    val personalLink: String,
    @SerializedName("projectExperience")
    val projectExperience: Boolean,
    @SerializedName("role")
    val role: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("peer")
    val peer: Int
)

data class ProfileMember(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("major")
    val major: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)