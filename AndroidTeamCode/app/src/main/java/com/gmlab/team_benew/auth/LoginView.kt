package com.gmlab.team_benew.auth

interface LoginView {
    //로그인 성공시
    fun onLoginSuccess()
    //로그인 실패시
    fun onLoginFailure()
    //인증 실패시
    fun onAuthenticationFailure()
}