package com.gmlab.team_benew.auth

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//만약 AuthRetrofitInterface에 /를 빼고 선언했다면 BASE_URL에는 /를 붙여줘야한다.
const val BASE_URL = "http://3.34.143.117:8080"

fun getOkHttpClient(): OkHttpClient{
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)        // 연결 실패 시 재시도
        .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))  // 커넥션 풀 설정
        .build()
}

//해당 함수는 Retrofit을 반환한다.
//Retrofit 객체 생성 시 구글이 만든 Gson을 이용하여 json을 자바 코드로 변환한다. 존나강력!
fun getRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}