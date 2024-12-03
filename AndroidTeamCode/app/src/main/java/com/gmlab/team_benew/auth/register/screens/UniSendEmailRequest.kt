package com.gmlab.team_benew.auth.register.screens

data class UniSendEmailRequest(
    val key: String,
    val email: String,
    val univName: String,
    val univ_check: Boolean
)
