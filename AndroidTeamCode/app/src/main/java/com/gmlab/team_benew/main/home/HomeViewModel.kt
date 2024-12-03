package com.gmlab.team_benew.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmlab.team_benew.project.ProjectListService
import com.gmlab.team_benew.project.ProjectListView
import com.gmlab.team_benew.project.ProjectResponse

class HomeViewModel : ViewModel() {
    private val _profileData = MutableLiveData<getProfilePreiviewData>()
    val profileData: LiveData<getProfilePreiviewData> get() = _profileData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _projectList = MutableLiveData<List<ProjectResponse>>()
    val projectList: LiveData<List<ProjectResponse>> get() = _projectList

    private val _mainProjectData = MutableLiveData<getMainProjectData>()
    val mainProjectData: LiveData<getMainProjectData> get() = _mainProjectData

    fun setProfileData(data: getProfilePreiviewData) {
        _profileData.value = data
        _isLoading.value = false
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun setProjectList(projects: List<ProjectResponse>) {
        _projectList.value = projects
        _isLoading.value = false
    }

    fun setMainProjectData(projectData: getMainProjectData) {
        _mainProjectData.value = projectData
        _isLoading.value = false
    }

    fun loadProjects(projectListService: ProjectListService) {
        setLoading(true)
        projectListService.setProjectListView(object : ProjectListView {
            override fun onProjectListSuccess(projects: List<ProjectResponse>) {
                setProjectList(projects)
            }

            override fun onProjectListEmpty() {
                setProjectList(emptyList())
            }

            override fun onProjectListFailure() {
                setLoading(false)
            }

            override fun onProjectListForbidden() {
                setLoading(false)
            }

            override fun onProjectListNotFound() {
                setLoading(false)
            }
        })
        projectListService.getProjects()
    }

}