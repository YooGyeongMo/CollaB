package com.gmlab.team_benew.auth

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRetrofitInterface {
    @POST("/register")
    fun signUp(@Body registerUser: RegisterUser) : Call<ResponseBody>


    @POST("/login")
    fun login(@Body user: User) : Call<LoginResult>


    // 스플래시에서 토큰 권한정보 확인시
    @GET("/user/get")
    fun adminGet(@Header("Authorization") bearerToken: String, @Query("account") account: String): Call<TokenGet>


    // 메인 액티비티에 가져오기 값
    @GET("/user/get")
    fun userGet(@Header("Authorization") bearerToken: String, @Query("account") account: String): Call<UserGet>

    @GET("/check/account")
    fun checkAccount(@Query("account") account: String): Call<AccountCheckGet>

    @GET("/check/email")
    fun checkEmail(@Query("email") email: String): Call<EmailCheckGet>

    @GET("/check/phone")
    fun checkPhone(@Query("phone") phone: String): Call<PhoneCheckGet>
}