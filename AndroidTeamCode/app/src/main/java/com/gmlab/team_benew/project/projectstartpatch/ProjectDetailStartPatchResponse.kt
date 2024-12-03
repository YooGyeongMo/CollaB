package com.gmlab.team_benew.project.projectstartpatch

import com.google.gson.annotations.SerializedName

data class ProjectDetailStartPatchResponse(

    @SerializedName("projectStartDate")
    val projectStartDate: String,

    @SerializedName("projectDeadlineDate")
    val projectDeadlineDate: String,

    @SerializedName("projectStarted")
    val projectStarted: Boolean

)