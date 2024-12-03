package com.gmlab.team_benew.main.mypage

import com.google.gson.annotations.SerializedName

data class getMypageData(
    @SerializedName("member")
    val member: MypageMember,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("photo")
    val photo: String
)

data class MypageMember(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("major")
    val major: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("account")
    val account: String,
    @SerializedName("password")
    val password: String
)