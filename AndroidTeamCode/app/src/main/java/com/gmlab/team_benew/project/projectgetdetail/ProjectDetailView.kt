package com.gmlab.team_benew.project.projectgetdetail

interface ProjectDetailView {
    fun onProjectDetailSuccess(response: GetProjectDeatilResponse)
    fun onProjectDetailFailure(statusCode: Int)

}