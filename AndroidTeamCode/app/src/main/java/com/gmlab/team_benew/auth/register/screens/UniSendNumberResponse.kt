package com.gmlab.team_benew.auth.register.screens

data class UniSendNumberResponse(
    val success: Boolean,
    val univName: String,
    val certified_email: String,
    val certified_date: String
)
