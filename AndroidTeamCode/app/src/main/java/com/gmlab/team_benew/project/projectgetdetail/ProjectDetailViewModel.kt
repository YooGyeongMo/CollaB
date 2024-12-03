package com.gmlab.team_benew.project.projectgetdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProjectDetailViewModel : ViewModel() {

    private val _projectDetail = MutableLiveData<GetProjectDeatilResponse>()
    val projectDetail: LiveData<GetProjectDeatilResponse> get() = _projectDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun setProjectDetail(projectDetail: GetProjectDeatilResponse) {
        _projectDetail.value = projectDetail
        _isLoading.value = false
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}