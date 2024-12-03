package com.gmlab.team_benew.matching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MatchingViewModel : ViewModel() {

    private val _matchingProfiles = MutableLiveData<MutableList<Profile>>()
    val matchingProfiles: LiveData<MutableList<Profile>> get() = _matchingProfiles

    private val _matchingId = MutableLiveData<MutableList<Long>>()
    val matchingId: LiveData<MutableList<Long>> get() = _matchingId

    private val _isFetching = MutableLiveData<Boolean>()
    val isFetching: LiveData<Boolean> get() = _isFetching

    private val _userCount = MutableLiveData<Int>()
    val userCount: LiveData<Int> get() = _userCount

    init {
        _matchingProfiles.value = mutableListOf()
        _matchingId.value = mutableListOf()
        _isFetching.value = false
        _userCount.value = 0
    }

    fun fetchMatchingProfiles(matchingService: MatchingService, userId: Int, onFailure: (String) -> Unit) {
        if (_isFetching.value == true) return
        _isFetching.value = true

        viewModelScope.launch {
            matchingService.getUserData(MatchRequestDto(uid1 = userId),
                onResponse = { newProfiles ->
                    if (newProfiles.isEmpty()) {
                        onFailure("더 이상 프로필이 없습니다.")
                    } else {
                        updateProfiles(newProfiles.map { it.profile }, newProfiles.map { it.matchId })
                    }
                    _isFetching.value = false
                },
                onFailure = { errorMessage ->
                    _isFetching.value = false
                    onFailure(errorMessage)
                }
            )
        }
    }

    fun incrementUserCount() {
        _userCount.value = (_userCount.value ?: 0) + 1
    }


    fun updateProfiles(newProfiles: List<Profile>, newIds: List<Long>) {
        val currentProfiles = _matchingProfiles.value ?: mutableListOf()
        val currentIds = _matchingId.value ?: mutableListOf()

        currentProfiles.addAll(newProfiles)
        currentIds.addAll(newIds)

        _matchingProfiles.value = currentProfiles
        _matchingId.value = currentIds
    }

    fun removeProfileAt(index: Int) {
        _matchingProfiles.value?.removeAt(index)
        _matchingId.value?.removeAt(index)
        _matchingProfiles.value = _matchingProfiles.value // Trigger observer update
        _matchingId.value = _matchingId.value // Trigger observer update
    }

}
