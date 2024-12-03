package com.gmlab.team_benew.auth.register.screens

data class UniSendNumberRequest(
    val key: String,
    val email: String,
    val univName: String,
    val code: Int
)
