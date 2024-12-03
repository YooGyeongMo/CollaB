package com.gmlab.team_benew.profile

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface putTechnologyRequest {
    @PUT("technology-levels/profile/{profileId}/technology/{technologyId}")
    fun putTechnology(
        @Header("Authorization") token: String,
        @Path("profileId") profileId: Int,
        @Path("technologyId") technologyId : Long,
        @Body request: putTechnologyData): Call<Boolean>
}