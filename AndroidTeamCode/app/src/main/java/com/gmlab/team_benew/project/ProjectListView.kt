package com.gmlab.team_benew.project

interface ProjectListView {
    fun onProjectListSuccess(projects: List<ProjectResponse>)
    fun onProjectListEmpty()
    fun onProjectListFailure()
    fun onProjectListForbidden()
    fun onProjectListNotFound()

}