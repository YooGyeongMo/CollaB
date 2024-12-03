package com.gmlab.team_benew.profile

import com.google.gson.annotations.SerializedName

data class postTechnologyData(
    @SerializedName("level")
    val level : Int,
    @SerializedName("technologyId")
    val technologyId: Long
)
