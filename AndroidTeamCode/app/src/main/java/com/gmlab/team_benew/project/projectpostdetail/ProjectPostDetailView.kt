package com.gmlab.team_benew.project.projectpostdetail

interface ProjectPostDetailView {
    fun onProjectPostSuccess()
    fun onProjectPostFailure(code: Int, message: String)
}