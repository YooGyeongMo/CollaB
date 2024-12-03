package com.gmlab.team_benew.auth.register.screens

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.AuthService
import com.gmlab.team_benew.auth.RegisterUser
import com.gmlab.team_benew.auth.SignUpView
import com.gmlab.team_benew.auth.register.RegisterViewModel
import com.gmlab.team_benew.start.IntroActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//794d30dc-6798-4e96-a63e-9953b5e6e8b4  <- unicert api키

class Register3Fragment : Fragment(), SignUpView {

    lateinit var btn_next : Button
    lateinit var btn_emailAuth : Button
    lateinit var et_email : EditText
    lateinit var et_emailAuthNumber : EditText
    lateinit var tv_email : TextView
    lateinit var btn_authNumber : Button
    lateinit var tv_authApiCheck : TextView

    lateinit var registerViewModel: RegisterViewModel

    val unicert_key : String = "794d30dc-6798-4e96-a63e-9953b5e6e8b4"
    var current_email : String = ""
    private var emailAuth : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register3, container, false)

        btn_next = view.findViewById(R.id.btn_thirdRegister_next)
        et_email = view.findViewById(R.id.et_thirdRegister_email)
        et_emailAuthNumber = view.findViewById(R.id.et_thirdRegister_authenticationNumber)
        tv_email = view.findViewById(R.id.tv_thirdRegister_email)
        btn_emailAuth = view.findViewById(R.id.btn_thirdRegister_emailAuth)
        btn_authNumber =view.findViewById(R.id.btn_thirdRegister_authNumber)
        tv_authApiCheck = view.findViewById(R.id.tv_thirdRegister_authApiCheck)

        registerViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        btn_emailAuth.setOnClickListener {
            try {
                Log.d("ThirdRegisterFragment", "Email Auth button clicked")
                unicert_auth()
            } catch (e: Exception) {
                Log.e("ThirdRegisterFragment", "Error in email auth click listener", e)
            }
        }

        btn_authNumber.setOnClickListener {
            try {
                Log.d("ThirdRegisterFragment", "Email Number Check button clicked")
                authNumberCheck()
            } catch (e: Exception) {
                Log.e("ThirdRegisterFragment", "Error in email number check click listener", e)
            }
        }

        btn_next.setOnClickListener {
            doRegister()
        }

        return view
    }

    private fun getRegisterUser() : RegisterUser {
        val id : String = registerViewModel.r_account
        val pwd : String = registerViewModel.r_password
        val name : String = registerViewModel.r_name
        val phoneNumber : String = registerViewModel.r_phoneNumber
        val birthday : String = registerViewModel.r_birthday
        val gender : String = registerViewModel.r_gender
        val major : String = registerViewModel.r_major


        return RegisterUser(id, pwd, name, gender, birthday, et_email.text.toString(), major, phoneNumber)
    }

    //회원가입 실행
    private fun doRegister(){
        if(emailAuth){
            val authService = AuthService()
            authService.setSignUpView(this)
            authService.signUp(getRegisterUser())
        }
        else{
            //이메일 인증이 안되어 있을 때
            if(et_emailAuthNumber.visibility == View.INVISIBLE){
                tv_email.text = "이메일 - 인증 필요."
                tv_email.setTextColor(Color.parseColor("#D1180B"))
            }
            else{
                tv_authApiCheck.text = "인증 필요."
                tv_authApiCheck.setTextColor(Color.parseColor("#D1180B"))
            }
        }
    }

    //이메일 발송 api
    private fun unicert_auth() {
        tv_email.text = "이메일"
        tv_email.setTextColor(Color.parseColor("#B4B4B4"))

        if(et_email.text.isEmpty()){
            tv_email.text = "이메일 - 비어있습니다."
            tv_email.setTextColor(Color.parseColor("#D1180B"))
            return
        }

        current_email = et_email.text.toString()

        val uni_api = UnicertRetrofitClient.instance
        val uni_request = UniSendEmailRequest(
            key = unicert_key,
            email = current_email,
            univName = "계명대학교",
            univ_check = true
        )

        uni_api.certify(uni_request).enqueue(object : Callback<UniSendEmailResponse> {
            override fun onResponse(call: Call<UniSendEmailResponse>, response: Response<UniSendEmailResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        Log.d("ThirdRegisterFragment", "인증번호 발송")

                        tv_email.text = "이메일 - 인증번호 발송."
                        tv_email.setTextColor(Color.parseColor("#2DB400"))

                        tv_authApiCheck.visibility = View.VISIBLE
                        et_emailAuthNumber.visibility = View.VISIBLE
                        btn_authNumber.visibility = View.VISIBLE
                    } else {
                        Log.e("ThirdRegisterFragment", "Error: ${responseBody?.message}")
                        tv_email.text = "이메일 - 인증번호 발송 실패."
                        tv_email.setTextColor(Color.parseColor("#D1180B"))
                    }
                } else {
                    Log.e("ThirdRegisterFragment", "Server Error: ${response.code()}")
                    tv_email.text = "이메일 - 인증번호 발송 실패."
                    tv_email.setTextColor(Color.parseColor("#D1180B"))
                }
            }

            override fun onFailure(call: Call<UniSendEmailResponse>, t: Throwable) {
                Log.d("LOGIN_NETWORK/FAILURE", t.message.toString())
            }
        })
    }

    //인증번호 인증 api
    private fun authNumberCheck(){
        val uni_api = UnicertRetrofitClient.instance
        val uni_request = UniSendNumberRequest(
            key = unicert_key,
            email = current_email,
            univName = "계명대학교",
            code = Integer.parseInt(et_emailAuthNumber.text.toString())
        )

        uni_api.certifycode(uni_request).enqueue(object : Callback<UniSendNumberResponse> {
            override fun onResponse(call: Call<UniSendNumberResponse>, response: Response<UniSendNumberResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        Log.d("ThirdRegisterFragment", "인증번호 인증 성공")
                        tv_authApiCheck.text = "인증 성공"
                        tv_authApiCheck.setTextColor(Color.parseColor("#2DB400"))


                        emailAuth = true
                    } else {
                        Log.e("ThirdRegisterFragment", "인증번호 오류")
                        tv_authApiCheck.text = "인증 실패"
                        tv_authApiCheck.setTextColor(Color.parseColor("#D1180B"))
                    }
                } else {
                    Log.e("ThirdRegisterFragment", "서버 오류")
                    tv_authApiCheck.text = "인증 실패"
                    tv_authApiCheck.setTextColor(Color.parseColor("#D1180B"))

                }
            }

            override fun onFailure(call: Call<UniSendNumberResponse>, t: Throwable) {
                Log.d("LOGIN_NETWORK/FAILURE", t.message.toString())
            }
        })
    }

    override fun onSignUpSuccess() {
        val intent = Intent(requireContext(), IntroActivity::class.java)
        intent.putExtra("showAlert", true)
        startActivity(intent)
        activity?.finish()
    }

    override fun onSignUpFailure() {
    }
}