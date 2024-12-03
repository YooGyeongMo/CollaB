package com.gmlab.team_benew.matching.projectmatchingfind

import com.google.gson.annotations.SerializedName

data class ProjectFindingAllResponse(
    @SerializedName("projectId")
    val projectId: Long,

    @SerializedName("projectName")
    val projectName: String,

    @SerializedName("numberOfMembers")
    val numberOfMembers: Int,

    @SerializedName("projectStartDate")
    val startDate: String,

    @SerializedName("projectManager")
    val projectManager: Long
)