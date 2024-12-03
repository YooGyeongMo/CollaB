package com.gmlab.team_benew.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProjectListViewModel : ViewModel() {
    private val _projects = MutableLiveData<List<ProjectResponse>>()
    val projects: LiveData<List<ProjectResponse>> get() = _projects

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun setProjects(data: List<ProjectResponse>) {
        _projects.value = data
        _isLoading.value = false
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun loadProjects(service: ProjectListService) {
        setLoading(true)
        service.getProjects()
    }
}