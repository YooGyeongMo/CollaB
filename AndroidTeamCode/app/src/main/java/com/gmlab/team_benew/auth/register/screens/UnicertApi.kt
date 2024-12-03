package com.gmlab.team_benew.auth.register.screens

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UnicertApi {
    @POST("api/v1/certify")
    fun certify(@Body request: UniSendEmailRequest): Call<UniSendEmailResponse>

    @POST("api/v1/certifycode")
    fun certifycode(@Body request: UniSendNumberRequest): Call<UniSendNumberResponse>
}