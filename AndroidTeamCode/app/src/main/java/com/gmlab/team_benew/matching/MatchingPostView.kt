package com.gmlab.team_benew.matching

interface MatchingPostView {

    fun onMatchingPostSuccess()

    fun onMatchingPostFailure()

    fun onMatchingLikePatchSuccess()

    fun onMatchingLikePatchFailure()

    fun onMatchingUnLikePatchSuccess()

    fun onMatchingUnLikePatchFailure()

    fun onMatchingRequestFailure(errorMessage: String)

}