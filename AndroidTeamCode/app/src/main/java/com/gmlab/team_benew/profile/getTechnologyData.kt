package com.gmlab.team_benew.profile

import com.google.gson.annotations.SerializedName

data class getTechnologyData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("technology")
    val technology: Technology,
)

data class Technology(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)