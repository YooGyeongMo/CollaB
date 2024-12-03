package com.gmlab.team_benew.matching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmlab.team_benew.project.ProjectResponse

class SharedViewModel : ViewModel() {

    private val _projects = MutableLiveData<List<ProjectResponse>>()
    val projects: LiveData<List<ProjectResponse>> get() = _projects

    private val _selectedProject = MutableLiveData<ProjectResponse>()
    val selectedProject: LiveData<ProjectResponse> get() = _selectedProject

    private val _ownedProjectIds = MutableLiveData<Set<Long>>()
    val ownedProjectIds: LiveData<Set<Long>> get() = _ownedProjectIds

    fun setProjects(projects: List<ProjectResponse>) {
        _projects.value = projects
        _ownedProjectIds.value = projects.map { it.projectId }.toSet()
    }

    fun setSelectedProject(project: ProjectResponse) {
        _selectedProject.value = project
    }
}
