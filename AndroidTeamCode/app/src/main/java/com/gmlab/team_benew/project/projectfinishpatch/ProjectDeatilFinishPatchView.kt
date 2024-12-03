package com.gmlab.team_benew.project.projectfinishpatch

interface ProjectDeatilFinishPatchView {
    fun onSuccessProjectFinishPatch(response: ProjectDetailFinishPatchResponse)
    fun onFailureProjectFinishPatch(message: String)
}