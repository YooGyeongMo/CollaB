package com.gmlab.team_benew.auth

interface ReLoginView {
    fun onReLoginSuccess()
    //로그인 실패시
    fun onReLoginFailure()
}