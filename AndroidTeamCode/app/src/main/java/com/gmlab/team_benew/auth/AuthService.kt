package com.gmlab.team_benew.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.gmlab.team_benew.auth.register.screens.Register3Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var reloginView: ReLoginView

    fun setSignUpView(signUpView: Register3Fragment) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setReLoginView(reloginView: ReLoginView) {
        this.reloginView =reloginView
    }

    fun signUp(registerUser: RegisterUser) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.signUp(registerUser).enqueue(object : Callback<ResponseBody> {
            //응답이 왔을때
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("NETWORK_SIGNUP/SUCCESS", response.toString())
                when (response.code()) {
                    200 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure()

                }

            }

            //실패 했을때
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("SIGNUP_NETWORK/FAILURE", t.message.toString()) // 비동기 작업
            }


        }) // enqueue에서 응답을 처리함.

        Log.d("SIGNUP", "비동기 함수 작동완료 ~!")
    }

    fun login(user: User, context: Context) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.login(user).enqueue(object : Callback<LoginResult> {
            //응답이 왔을때
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                Log.d("NETWORK_LOGIN/SUCCESS", response.toString())
                when (response.code()) {
                    200 -> {
                        //응답 바디가 널이 아닐때 let블록실행
                        response.body()?.let{ result->
                            val currentToken = getCurrentToken(context)
                            if( currentToken != result.token) {
                            saveLoginInfo(context, result.id, result.account, result.token, result.name)
                        }
                            loginView.onLoginSuccess()
                        }
//
                    }
                    401 -> {
                        loginView.onAuthenticationFailure()
                    }
                    else ->
                        loginView.onLoginFailure()
                }
            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                Log.d("LOGIN_NETWORK/FAILURE", t.message.toString()) // 비동기 작업
                Toast.makeText(context, "네트워크 에러... 로그인을 다시 시도하세요.", Toast.LENGTH_SHORT).show()
            }

        })

        Log.d("LOGIN", "비동기 함수 작동완료 ~!")
    }

//    private fun relogin(user: User, context: Context) {
//        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
//        authService.login(user).enqueue(object: Callback<LoginResult>{
//            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
//                Log.d("NETWORK_RELOGIN/SUCCESS", response.toString())
//                if(response.isSuccessful){
//                    response.body()?.let{
//                        //새로운 로그인정보를 SharedPreferences에 저장
//                        saveLoginInfo(context, it.id, it.account, it.token,it.name)
//                        reloginView.onReLoginSuccess()
//                    }
//                }
//                else{
//                    reloginView.onReLoginFailure()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
//                Log.d("NETWORK_RELOGIN/SUCCESS","다시 로그인 성공!")
//            }
//        })
//    }

    // null 이 아닐 때 let 구분이 실행되도록 설정 요청 후 응답받는 값이 null이 아닐때 설정.
    // 최초 로그인시 저장됩니다.
    private fun saveLoginInfo(context: Context, id: Int?, account: String?, token: String?, name: String?)
    {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            id?.let { putInt("loginId", it) }
            account?.let { putString("userAccount", it) }
            token?.let { putString("userToken", it) }
            name?.let {putString("cachedUserName", it)}
            apply()
        }
    }

    private fun getCurrentToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }
}