package com.gmlab.team_benew.auth.register.screens

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.AccountCheckGet
import com.gmlab.team_benew.auth.AuthRetrofitInterface
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.auth.register.RegisterActivity
import com.gmlab.team_benew.auth.register.RegisterViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register1Fragment : Fragment() {

    private lateinit var btn_Next: Button
    private lateinit var et_name: EditText
    private lateinit var et_id: EditText
    private lateinit var et_password: EditText
    private lateinit var et_passwordCheck: EditText
    private lateinit var tv_name: TextView
    private lateinit var tv_id: TextView
    private lateinit var tv_password: TextView
    private lateinit var tv_passwordCheck: TextView
    private lateinit var btn_idCheck: Button

    private var idCheck: Boolean = false

    lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register1, container, false)

        btn_Next = view.findViewById(R.id.btn_firstRegister_next)
        et_name = view.findViewById(R.id.et_firstRegister_name)
        et_id = view.findViewById(R.id.et_firstRegister_id)
        et_password = view.findViewById(R.id.et_firstRegister_password)
        et_passwordCheck = view.findViewById(R.id.et_firstRegister_passwordCheck)
        tv_name = view.findViewById(R.id.tv_firstRegister_name)
        tv_id = view.findViewById(R.id.tv_firstRegister_id)
        tv_password = view.findViewById(R.id.tv_firstRegister_password)
        tv_passwordCheck = view.findViewById(R.id.tv_firstRegister_passwordCheck)
        btn_idCheck = view.findViewById(R.id.btn_firstRegister_idCheck)

        registerViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        // id 중복체크
        btn_idCheck.setOnClickListener {
            accountCheck()
        }

        // 다음 화면으로 이동하는 리스너 설정
        btn_Next.setOnClickListener {
            next()
        }

        return view
    }

    //다음 화면 이동 호출
    private fun next() {

        textView_init()

        var checkCount: Boolean
        checkCount = true

        if (et_name.text.toString().isEmpty()) {
            tv_name.text = "이름 - 비어있습니다."
            tv_name.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if (et_id.text.toString().isEmpty()) {
            tv_id.text = "아이디 - 비어있습니다."
            tv_id.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if (!idCheck) {
            tv_id.text = "아이디 - 중복체크를 해주세요."
            tv_id.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if (et_password.text.toString().isEmpty()) {
            tv_password.text = "비밀번호 - 비어있습니다."
            tv_password.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if (et_password.text.toString() != et_passwordCheck.text.toString()) {
            tv_passwordCheck.text = "비밀번호 확인 - 일치하지 않습니다."
            tv_passwordCheck.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        registerViewModel.r_account = et_id.text.toString()
        registerViewModel.r_password = et_password.text.toString()
        registerViewModel.r_name = et_name.text.toString()

        if (checkCount) {
            // RegisterActivity의 인스턴스를 얻어옴
            val registerActivity = RegisterActivity.getInstance()
            // RegisterActivity가 null이 아니면 progress_increase 함수 호출
            registerActivity?.progress_increase()
            // ViewPager2의 현재 인덱스를 가져옴
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp_registerViewPager)
            val currentIndex = viewPager.currentItem

            // 다음 화면으로 이동
            viewPager.setCurrentItem(currentIndex + 1, true)
        }
    }

    private fun textView_init() {
        tv_name.text = "이름"
        tv_name.setTextColor(Color.parseColor("#B4B4B4"))

        tv_id.text = "아이디"
        tv_id.setTextColor(Color.parseColor("#B4B4B4"))

        tv_password.text = "비밀번호"
        tv_password.setTextColor(Color.parseColor("#B4B4B4"))

        tv_passwordCheck.text = "비밀번호 확인"
        tv_passwordCheck.setTextColor(Color.parseColor("#B4B4B4"))
    }

    private fun accountCheck() {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        val account = et_id.text.toString()  // EditText에서 입력값 가져오기

        // 아이디 정규식 패턴
        val idPattern = "[a-zA-Z0-9]+"

        // 아이디가 정규식 패턴과 일치하지 않는 경우
        if (!et_id.text.toString().matches(idPattern.toRegex())) {
            tv_id.text = "아이디 - 알파벳과 숫자만 사용하여 주세요."
            tv_id.setTextColor(Color.parseColor("#D1180B"))
            return
        }

        authService.checkAccount(account).enqueue(object : Callback<AccountCheckGet> {
            // 응답이 왔을 때
            override fun onResponse(
                call: Call<AccountCheckGet>,
                response: Response<AccountCheckGet>
            ) {
                Log.d("NETWORK_SIGNUP/SUCCESS", response.toString())
                if (response.isSuccessful) {
                    val accountCheckGet = response.body()
                    if (accountCheckGet != null) {
                        if (accountCheckGet.duplication) {
                            // 아이디가 중복일 때
                            tv_id.text = "아이디 - 중복된 아이디입니다."
                            tv_id.setTextColor(Color.parseColor("#D1180B"))
                            idCheck = false

                            Log.d("NETWORK_SIGNUP/RESPONSE", "중복된 아이디: ${accountCheckGet.message}")
                        } else {
                            // 아이디가 사용 가능할 때
                            tv_id.text = "아이디 - 사용가능한 아이디입니다."
                            tv_id.setTextColor(Color.parseColor("#2DB400"))
                            idCheck = true

                            Log.d(
                                "NETWORK_SIGNUP/RESPONSE",
                                "사용 가능한 아이디: ${accountCheckGet.message}"
                            )
                        }
                    } else {
                        Log.d("NETWORK_SIGNUP/RESPONSE", "응답 내용 없음")
                    }
                } else {
                    // 실패 시 처리 로직 추가
                    Log.d("NETWORK_SIGNUP/ERROR", response.errorBody()?.string() ?: "에러 내용 없음")
                }
            }

            // 실패했을 때
            override fun onFailure(call: Call<AccountCheckGet>, t: Throwable) {
                Log.d("SIGNUP_NETWORK/FAILURE", t.message.toString()) // 비동기 작업
            }
        })

        Log.d("SIGNUP", "비동기 함수 작동완료 ~!")
    }
}