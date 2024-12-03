package com.gmlab.team_benew.main.home.firstSetting

import com.google.gson.annotations.SerializedName

data class postFirstSettingData(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("photo")
    val photo : String?
)
