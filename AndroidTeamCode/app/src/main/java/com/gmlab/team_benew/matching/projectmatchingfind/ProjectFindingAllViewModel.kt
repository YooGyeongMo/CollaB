package com.gmlab.team_benew.matching.projectmatchingfind

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmlab.team_benew.matching.SharedViewModel
import kotlinx.coroutines.launch

class ProjectFindingAllViewModel(
    private val sharedViewModel: SharedViewModel
) : ViewModel(), ProjectFindingAllGetView {

    private val _projects = MutableLiveData<List<ProjectFindingAllResponse>>()
    val projects: LiveData<List<ProjectFindingAllResponse>> get() = _projects

    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    private val projectFindingAllGetService = ProjectFindingAllGetService()

    fun fetchProjects(token: String) {
        viewModelScope.launch {
            isLoading.value = true
            projectFindingAllGetService.getAllProject(token, this@ProjectFindingAllViewModel)
        }
    }

    override fun onSuccessProjectFindingGet(projects: List<ProjectFindingAllResponse>) {
        val ownedProjectIds = sharedViewModel.ownedProjectIds.value ?: emptySet()
        val filteredProjects = projects.filter { it.projectId !in ownedProjectIds }
        _projects.value = filteredProjects
        isLoading.value = false
    }

    override fun onUnauthorized(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    override fun onForbidden(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    override fun onNotFound(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    override fun onFailureProjectFindingGet(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }
}
