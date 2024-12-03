package com.gmlab.team_benew.main.mypage

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gmlab.team_benew.R
import com.gmlab.team_benew.profile.getProfileDetailData
import com.gmlab.team_benew.profile.getProfileDetailRequest
import com.gmlab.team_benew.profile.postProfileDetailData
import com.gmlab.team_benew.profile.postProfileDetailRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var img_picture : ImageView
private lateinit var tv_name : TextView
private lateinit var tv_nickname : TextView
private lateinit var et_email : EditText
private lateinit var et_phoneNumber : EditText
private lateinit var spn_major : Spinner
private lateinit var btn_email : Button
private lateinit var btn_phoneNumber : Button
private lateinit var btn_major : Button
private lateinit var tv_gender : TextView
private lateinit var tv_account : TextView
private lateinit var tv_birthday : TextView

class MypageFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)
        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        val token = sharedPref?.getString("userToken", "")
        val account = sharedPref?.getString("userAccount", "")
        val memberId = sharedPref?.getInt("loginId", 0)

        initLaoutId(view)

        img_picture.clipToOutline = true
        img_picture.visibility = View.VISIBLE

        et_phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        if (token != null) {
            if (memberId != null) {
                getDataToServer(token, memberId)
            }
        }

        btn_email.setOnClickListener {
            if (token != null) {
                if (memberId != null) {
                    putEmail(token, memberId)
                }
            }
        }

        btn_major.setOnClickListener {
            if (token != null) {
                if (memberId != null) {
                    putMajor(token, memberId)
                }
            }
        }

        btn_phoneNumber.setOnClickListener {
            if (token != null) {
                if (memberId != null) {
                    putPhoneNumber(token, memberId)
                }
            }
        }

        return view
    }

    private fun putMajor(token : String, memberId: Int) {
        if(memberId == null){
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.143.117:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(putMypageMajorRequest::class.java)

        val request = putMypageMajorData(spn_major.selectedItem.toString())

        val call: Call<Boolean> = apiService.putMypageMajor("Bearer $token", memberId, request)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("API_CALL_FAILURE", "API Call Failed", t)
                Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun putPhoneNumber(token : String, memberId: Int) {
        if(memberId == null){
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.143.117:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(putMypagePhoneNumberRequest::class.java)

        val request = putMypagePhoneNumberData(et_phoneNumber.text.toString())

        val call = apiService.putMypagePhoneNumber("Bearer $token", memberId, request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when(response.code()){
                    200 -> {
                        Log.d("PUT/SUCCESS","${response.code()}휴대폰번호 변경성공")
                    }
                }
//                if (response.isSuccessful) {
//                    Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show()
//                } else{
//                    Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_LONG).show()
//                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API_CALL_FAILURE", "API Call Failed", t)
                Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun putEmail(token : String, memberId: Int) {
        if(memberId == null){
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.143.117:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(putMypageEmailRequest::class.java)

        val request = putMypageEmailData(et_email.text.toString())

        val call: Call<String> = apiService.putMypageEmail("Bearer $token", memberId, request)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL_FAILURE", "API Call Failed", t)
                Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getDataToServer(token : String, memberId: Int){
        if(memberId == null){
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.143.117:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(getMypageRequest::class.java)

        val call: Call<getMypageData> = apiService.getMypage("Bearer $token", memberId)

        call?.enqueue(object : Callback<getMypageData> {
            override fun onResponse(call: Call<getMypageData>, response: Response<getMypageData>) {
                if (response.isSuccessful) {
                    val profileData: getMypageData? = response.body()
                    profileData?.let {
                        val name = it.member.name
                        val nickname = it.nickname
                        val email = it.member.email
                        val phoneNumber = it.member.phoneNumber
                        val major = it.member.major
                        val photo = it.photo
                        val gender = it.member.gender
                        val account = it.member.account
                        val birthday = it.member.birthday

                        tv_name.text = name
                        tv_nickname.text = nickname
                        et_email.setText(email)
                        et_phoneNumber.setText(phoneNumber)
                        tv_gender.text = gender
                        tv_account.text = account
                        tv_birthday.text = birthday

                        if(major == "전공자"){
                            spn_major.setSelection(0)
                        }
                        else{
                            spn_major.setSelection(1)
                        }

                        if(photo != "") {
                            setBitmapFromBase64(photo, img_picture)
                        }

                    }
                }
                else {
                }
            }
            override fun onFailure(call: Call<getMypageData>, t: Throwable) {

            }
        })
    }

    private fun setBitmapFromBase64(base64String: String, imageview: ImageView) {
        if (base64String != null) {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            imageview.setImageBitmap(bitmap)
        } else {

        }
    }

    private fun initLaoutId(view : View) {
        img_picture = view.findViewById(R.id.imgv_mypage_picture)
        tv_name = view.findViewById(R.id.tv_mypage_name)
        tv_nickname = view.findViewById(R.id.tv_mypage_nickname)
        et_email = view.findViewById(R.id.et_mypage_email)
        et_phoneNumber = view.findViewById(R.id.et_mypage_phoneNumber)
        spn_major = view.findViewById(R.id.spn_mypage_major)
        btn_email = view.findViewById(R.id.btn_mypage_email)
        btn_phoneNumber = view.findViewById(R.id.btn_mypage_phoneNumber)
        btn_major = view.findViewById(R.id.btn_mypage_major)
        tv_gender = view.findViewById(R.id.tv_mypage_gender)
        tv_account = view.findViewById(R.id.tv_mypage_account)
        tv_birthday = view.findViewById(R.id.tv_mypage_birthday)
    }
}