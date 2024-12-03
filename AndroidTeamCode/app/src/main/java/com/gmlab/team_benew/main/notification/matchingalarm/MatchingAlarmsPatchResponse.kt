package com.gmlab.team_benew.main.notification.matchingalarm

import com.gmlab.team_benew.matching.Profile
import com.google.gson.annotations.SerializedName

data class MatchingAlarmsResponse (
    @SerializedName("matchId")
    val matchId: Long,

    @SerializedName("matchSuccess")
    val matchSuccess: Boolean,

    @SerializedName("profile")
    val profile: Profile,

    )

data class Profile (
    @SerializedName("id")
    val id: Long
)