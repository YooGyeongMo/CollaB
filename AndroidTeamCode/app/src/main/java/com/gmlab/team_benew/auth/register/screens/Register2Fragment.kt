package com.gmlab.team_benew.auth.register.screens

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.AuthRetrofitInterface
import com.gmlab.team_benew.auth.PhoneCheckGet
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.auth.register.RegisterActivity
import com.gmlab.team_benew.auth.register.RegisterViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class Register2Fragment : Fragment() {

    private lateinit var btn_birthday : Button
    private lateinit var et_phone : EditText
    private lateinit var rg_gender : RadioGroup
    private lateinit var rg_major : RadioGroup
    private lateinit var btn_next : Button
    private lateinit var tv_birthday : TextView
    private lateinit var tv_phone : TextView
    private lateinit var tv_genderCheck : TextView
    private lateinit var tv_majorCheck : TextView
    private lateinit var btn_phoneCheck : Button

    private var phoneCheckCount : Boolean = false

    lateinit var registerViewModel: RegisterViewModel

    var gender : String = ""
    var major : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register2, container, false)

        btn_birthday = view.findViewById(R.id.btn_secondRegister_birthday)
        btn_phoneCheck = view.findViewById(R.id.btn_secondRegister_phoneCheck)
        et_phone = view.findViewById(R.id.et_secondRegister_phone)
        rg_gender = view.findViewById(R.id.rg_secondRegister_gender)
        rg_major = view.findViewById(R.id.rg_secondRegister_major)
        btn_next = view.findViewById(R.id.btn_secondScreen_next)
        tv_birthday = view.findViewById(R.id.tv_secondRegister_birthday)
        tv_phone = view.findViewById(R.id.tv_secondRegister_phone)
        tv_genderCheck = view.findViewById(R.id.tv_secondRegister_genderCheck)
        tv_majorCheck = view.findViewById(R.id.tv_secondRegister_majorCheck)

        registerViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        btn_birthday.setOnClickListener {
            showDatePickerDialog()
        }

        btn_phoneCheck.setOnClickListener{
            phoneCheck()
        }

        et_phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        rg_gender.setOnCheckedChangeListener { group, checkedId ->
            gender = when (checkedId) {
                R.id.rb_secondRegister_male -> "남성"
                R.id.rb_secondRegister_female -> "여성"
                else -> ""
            }
        }

        rg_major.setOnCheckedChangeListener{ group, checkedId ->
            major = when(checkedId){
                R.id.rb_secondRegister_true -> "전공자"
                R.id.rb_secondRegister_false -> "비전공자"
                else -> ""
            }
        }

        btn_next.setOnClickListener {
            next()
        }

        return view
    }

    //다음 화면
    private fun next(){
        textView_init()

        var checkCount = true

        if(btn_birthday.text.isEmpty()){
            tv_birthday.text = "생년월일 - 비어있습니다."
            tv_birthday.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if(!phoneCheckCount){
            tv_phone.text = "전화번호 - 중복체크를 해주세요."
            tv_phone.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if(rg_gender.checkedRadioButtonId == -1){
            tv_genderCheck.text = "선택하여 주세요."
            tv_genderCheck.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if(rg_major.checkedRadioButtonId == -1){
            tv_majorCheck.text = "선택하여 주세요."
            tv_majorCheck.setTextColor(Color.parseColor("#D1180B"))
            checkCount = false
        }

        if(checkCount){

            registerViewModel.r_birthday = btn_birthday.text.toString()
            registerViewModel.r_phoneNumber = et_phone.text.toString()
            registerViewModel.r_gender = gender
            registerViewModel.r_major = major

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

    private fun textView_init(){
        tv_birthday.text = "생년월일"
        tv_birthday.setTextColor(Color.parseColor("#B4B4B4"))

        tv_phone.text = "전화번호"
        tv_phone.setTextColor(Color.parseColor("#B4B4B4"))

        tv_genderCheck.text = ""
        tv_genderCheck.setTextColor(Color.parseColor("#B4B4B4"))

        tv_majorCheck.text = ""
        tv_majorCheck.setTextColor(Color.parseColor("#B4B4B4"))
    }

    //달력
    private fun showDatePickerDialog(){
        val calendar = Calendar.getInstance()

        calendar.set(2000, 1, 1)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
                btn_birthday.text = selectedDate
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun phoneCheck() {

        if(et_phone.text.length != 13){
            tv_phone.text = "전화번호 - 형식이 올바르지 않습니다."
            tv_phone.setTextColor(Color.parseColor("#D1180B"))
            return
        }

        if(et_phone.text.isEmpty()){
            tv_phone.text = "전화번호 - 비어있습니다."
            tv_phone.setTextColor(Color.parseColor("#D1180B"))
            return
        }

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        val phone = et_phone.text.toString()  // EditText에서 입력값 가져오기

        authService.checkPhone(phone).enqueue(object : Callback<PhoneCheckGet> {
            // 응답이 왔을 때
            override fun onResponse(
                call: Call<PhoneCheckGet>,
                response: Response<PhoneCheckGet>
            ) {
                Log.d("NETWORK_SIGNUP/SUCCESS", response.toString())
                if (response.isSuccessful) {
                    val phoneCheckGet = response.body()
                    if (phoneCheckGet != null) {
                        if (phoneCheckGet.duplication) {
                            // 핸드폰 번호가 중복일 때
                            tv_phone.text = "전화번호 - 중복된 전화번호입니다."
                            tv_phone.setTextColor(Color.parseColor("#D1180B"))
                            phoneCheckCount = false

                            Log.d("NETWORK_SIGNUP/RESPONSE", "중복된 전화번호: ${phoneCheckGet.message}")
                        } else {
                            // 전화번호가 사용 가능할 때
                            tv_phone.text = "전화번호 - 사용가능한 전화번호입니다."
                            tv_phone.setTextColor(Color.parseColor("#2DB400"))
                            phoneCheckCount = true

                            Log.d(
                                "NETWORK_SIGNUP/RESPONSE",
                                "사용 가능한 아이디: ${phoneCheckGet.message}"
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
            override fun onFailure(call: Call<PhoneCheckGet>, t: Throwable) {
                Log.d("SIGNUP_NETWORK/FAILURE", t.message.toString()) // 비동기 작업
            }
        })

        Log.d("SIGNUP", "비동기 함수 작동완료 ~!")
    }
}