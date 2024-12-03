package com.gmlab.team_benew.matching.projectmatchingfind

interface ProjectFindingAllGetView {
    fun onSuccessProjectFindingGet(projects: List<ProjectFindingAllResponse>)
    fun onUnauthorized(message: String)
    fun onForbidden(message: String)
    fun onNotFound(message: String)
    fun onFailureProjectFindingGet(message: String)
}