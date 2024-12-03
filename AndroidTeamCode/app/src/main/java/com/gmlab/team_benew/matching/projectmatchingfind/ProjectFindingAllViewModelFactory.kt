package com.gmlab.team_benew.matching.projectmatchingfind

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmlab.team_benew.matching.SharedViewModel

class ProjectFindingAllViewModelFactory(
    private val sharedViewModel: SharedViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectFindingAllViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectFindingAllViewModel(sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
