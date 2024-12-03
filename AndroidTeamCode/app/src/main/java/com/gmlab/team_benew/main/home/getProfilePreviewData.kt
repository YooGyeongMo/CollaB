package com.gmlab.team_benew.main.home

import com.google.gson.annotations.SerializedName

data class getProfilePreiviewData(
    @SerializedName("instruction") val instruction: String,
    @SerializedName("memberId") val memberId: Int,
    @SerializedName("memberName") val memberName: String,
    @SerializedName("memberEmail") val memberEmail: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("personalLink") val personalLink: String,
    @SerializedName("projectExperience") val projectExperience: Boolean,
    @SerializedName("role") val role: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("peer") val peer: Int
    )

data class getMainProjectData(
    @SerializedName("projectName") val projectName: String,
    @SerializedName("projectRateOfProgress") val projectRateOfProgress: Double
)