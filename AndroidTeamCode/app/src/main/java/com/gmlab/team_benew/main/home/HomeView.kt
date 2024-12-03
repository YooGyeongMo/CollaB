package com.gmlab.team_benew.main.home


interface HomeView {

    fun onHomeGetSuccess(profileData: getProfilePreiviewData)

    fun onHomeGetFailure()

    fun onMainProjectGetSuccess(projectData: getMainProjectData)
    fun onMainProjectGetFailure(statusCode: Int)
}