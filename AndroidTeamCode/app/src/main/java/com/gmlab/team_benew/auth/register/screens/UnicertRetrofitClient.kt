package com.gmlab.team_benew.auth.register.screens

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UnicertRetrofitClient {
    private const val BASE_URL = "https://univcert.com/"

    val instance: UnicertApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UnicertApi::class.java)
    }
}