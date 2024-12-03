package com.gmlab.team_benew.project.projectgetmember

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectGetMemberViewModel : ViewModel() {
    private val _projectMembers = MutableLiveData<List<ProjectMemberResponse>>()
    val projectMembers: LiveData<List<ProjectMemberResponse>> = _projectMembers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getProjectMembers(context: Context, projectId: Int) {
        _isLoading.value = true
        val service = ProjectGetMemberService(context)
        service.getProjectMembers(projectId, object : ProjectGetMemberService.ProjectGetMemberCallback {
            override fun onSuccess(members: List<ProjectMemberResponse>) {
                _isLoading.value = false
                _projectMembers.value = members
            }

            override fun onFailure(errorCode: Int) {
                _isLoading.value = false
                _errorMessage.value = "Error: $errorCode"
            }
        })
    }
}
