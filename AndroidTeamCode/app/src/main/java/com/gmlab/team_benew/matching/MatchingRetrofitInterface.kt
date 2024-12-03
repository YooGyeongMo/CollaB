package com.gmlab.team_benew.matching

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MatchingRetrofitInterface {
    //매치 생성 post
    @POST("/api/post/match")
    fun postCreateMatch(
        @Header("Authorization") bearerToken: String,
        @Body matchRequestDto: MatchRequestDto
    ): Call<List<MatchingResponse>>

    //매치 좋아요 스와이프 patch
    @PATCH("/api/patch/match/like/{matchId}")
    fun patchLikeMatch(
        @Header("Authorization") bearerToken: String,
        @Path("matchId") matchId: Long,
        @Body request: LikeMatchRequest
    ): Call<MatchingResponse>

    //매치 싫어요 스와이프 patch
    @PATCH("/api/patch/match/dislike/{matchId}")
    fun patchDisLikeMatch(
        @Header("Authorization") bearerToken: String,
        @Path("matchId") matchId: Long
    ): Call<MatchingResponse>
}