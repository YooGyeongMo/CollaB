package com.gmlab.team_benew.main

interface MainView {

    fun onMainGetSuccess()
    // 토큰 유효성 실패시
    fun onMainGetFailure()

}