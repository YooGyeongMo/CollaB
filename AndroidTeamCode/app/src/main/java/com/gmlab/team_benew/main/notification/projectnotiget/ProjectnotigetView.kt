package com.gmlab.team_benew.main.notification.projectnotiget


interface ProjectnotigetView {

    fun onProjectNotiSuccess(response: GetProjectNotiResponse)
    fun onProjectNotiFailure(statusCode: Int)

}