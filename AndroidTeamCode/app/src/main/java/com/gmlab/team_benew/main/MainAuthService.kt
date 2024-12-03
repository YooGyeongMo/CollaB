package com.gmlab.team_benew.main

import android.util.Log
import com.gmlab.team_benew.auth.AuthRetrofitInterface

import com.gmlab.team_benew.auth.UserGet
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.main.home.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainAuthService(private val context: HomeFragment) {

    private lateinit var mainView: MainView

    private var userNameCallback: UserNameCallback? = null

    fun setMainView(mainView: MainView) {
        this.mainView = mainView
    }


    fun setUserNameCallback(callback: UserNameCallback) {
        this.userNameCallback = callback
    }

    fun getUserName(token: String, account: String){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        val bearerToken = "Bearer $token"
        authService.userGet(bearerToken, account).enqueue(object : Callback<UserGet> {
            override fun onResponse(call: Call<UserGet>, response: Response<UserGet>) {
                when(response.code()){
                    200 -> {
                        var username =  response.body()?.name?.let{it + "님"} ?:""
                        userNameCallback?.onUserNameReceived(username)
                        mainView.onMainGetSuccess()
                    }
                    401 -> {
                        mainView.onMainGetFailure()
                    }
                    else -> {
                        response.code()
                    }
                }

            }

            override fun onFailure(call: Call<UserGet>, t: Throwable) {
                Log.d("NETWORK/FAILURE","MAIN ACTIVITY 네트워크 연결실패")
            }

        })
    }


}