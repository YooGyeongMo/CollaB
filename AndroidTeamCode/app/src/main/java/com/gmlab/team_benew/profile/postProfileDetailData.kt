package com.gmlab.team_benew.profile

import com.google.gson.annotations.SerializedName

data class postProfileDetailData(
    @SerializedName("instruction")
    val instruction: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("projectExperience")
    val projectExperience: Boolean,
    @SerializedName("role")
    val role: String,
    @SerializedName("personalLink")
    val personalLink : String,
    @SerializedName("photo")
    val photo : String
)
