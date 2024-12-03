package com.gmlab.team_benew.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.gmlab.team_benew.R
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class ProfileDetailFragment: Fragment() {

    private lateinit var cv_bottom : CardView
    private lateinit var imgb_picture: ImageButton
    private lateinit var et_nickname: EditText
    private lateinit var et_introduce: EditText
    private lateinit var btn_modify: Button
    private lateinit var tv_gender : TextView
    private lateinit var spn_experience : Spinner
    private lateinit var et_role : EditText
    private lateinit var et_link : EditText
    private lateinit var tv_major : TextView
    private lateinit var btn_addSkill : Button
    private lateinit var img_peer : ImageView

    private lateinit var linear_skill : LinearLayout

    private lateinit var skillSpinner : Spinner
    private lateinit var LevelSpinner : Spinner

    private lateinit var technologyIdList : MutableList<Long>
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_detail, container, false)

        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        val token = sharedPref?.getString("userToken", "")
        val account = sharedPref?.getString("userAccount", "")
        val memberId = sharedPref?.getInt("loginId", 0)

        technologyIdList = mutableListOf()

        cv_bottom = view.findViewById(R.id.cv_profilecardDetail_bottom)
        imgb_picture = view.findViewById(R.id.imgb_profilecardDetail_picture)
        et_nickname = view.findViewById(R.id.et_profilecardDetail_nickname)
        et_introduce = view.findViewById(R.id.et_profilecardDetail_introduce)
        btn_modify = view.findViewById(R.id.btn_profilecardDetail_modify)
        tv_gender = view.findViewById(R.id.tv_profilecardDetail_gender)
        spn_experience = view.findViewById(R.id.spn_profilecardDetail_projectExperience)
        et_role = view.findViewById(R.id.et_profilecardDetail_role)
        et_link = view.findViewById(R.id.et_profilecardDetail_link)
        tv_major = view.findViewById(R.id.tv_profilecardDetail_major)
        linear_skill = view.findViewById(R.id.linear_profilecarddetail_skill)
        btn_addSkill = view.findViewById(R.id.btn_profilecard_addskill)
        img_peer = view.findViewById(R.id.imgv_profiledetail_peer)

        imgb_picture.clipToOutline = true
        imgb_picture.visibility = View.VISIBLE

        disableInputFields()

        val selectedView = spn_experience.selectedView as? TextView
        if (selectedView != null) {
            selectedView.setTextColor(Color.BLACK)
        }

        if (memberId != null) {
            if (token != null) {
                if(account != null) {
                    getProfileToServer(token, memberId)
                    getTechnology(token, account)
                }
            }
        }


        imgb_picture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }

        var isEditMode = false
        btn_addSkill.setOnClickListener {

            if (!isEditMode) {

                // Edit mode 활성화
                btn_addSkill.setBackgroundResource(R.drawable.save_or_check_btn)
                skillAdd()
                isEditMode = true // isEditMode를 true로 설정
            }
            else {
                // Edit mode 비활성화
                btn_addSkill.setBackgroundResource(R.drawable.add_btn)
                if (account != null && token != null && memberId != null) {
                    skillFinish(token, account, memberId)
                }
                isEditMode = false // isEditMode를 false로 설정
            }
        }

        var isEditMode2 = false
        btn_modify.setOnClickListener {
            if (!isEditMode2) {

                enableInputFields()

                btn_modify.setBackgroundResource(R.drawable.save_or_check_btn)

                isEditMode2 = true

            } else {
                if (token != null && memberId != null) {
                        postProfileToServer(token, memberId)
                    }

                disableInputFields()


                // 버튼 배경을 "수정" 이미지로 변경
                btn_modify.setBackgroundResource(R.drawable.add_btn)

                // isEditMode를 false로 설정
                isEditMode2 = false
            }
        }

        cv_bottom.setOnClickListener {
            handleLinkClick()
        }

        return view
    }

    private fun handleLinkClick(){
        try {
            if(et_link.text.isNotEmpty()) {
                val link = et_link.text.toString()

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }
        catch(e : Exception)
        {
            Toast.makeText(requireContext(), "추가된 링크가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postProfileToServer(token : String, memberId : Int){

        var instruction = et_introduce.text.toString()
        var nickname = et_nickname.text.toString()
        var role = et_role.text.toString()
        var personalLink = et_link.text.toString()
        var photo = imageButtonToBase64(imgb_picture)

//      스피너 처리
        var projectExperience_value = spn_experience.selectedItem.toString()
        var projectExperience : Boolean
        if(projectExperience_value == "유")
        {
            projectExperience = true
        }
        else {
            projectExperience = false
        }

        if(memberId == null){
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.143.117:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(postProfileDetailRequest::class.java)

        val request = postProfileDetailData(instruction, nickname, projectExperience, role, personalLink, photo)

        val call: Call<ResponseBody> = apiService.postProfile("Bearer $token", memberId, request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API_CALL_FAILURE", "API Call Failed: ${t.localizedMessage}", t)
            }

        })
    }

    private fun getProfileToServer(token : String, memberId : Int){
        if(memberId == null){
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.143.117:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(getProfileDetailRequest::class.java)

        val call: Call<getProfileDetailData> = apiService.getProfile("Bearer $token", memberId)

        call?.enqueue(object : Callback<getProfileDetailData> {
            override fun onResponse(call: Call<getProfileDetailData>, response: Response<getProfileDetailData>) {
                if (response.isSuccessful) {
                    val profileData: getProfileDetailData? = response.body()
                    profileData?.let {
                        val nickname = it.nickname ?: ""
                        val instruction = it.instruction ?: ""
                        val personalLink = it.personalLink ?: ""
                        val projectExperience = it.projectExperience ?: false
                        val role = it.role ?: ""
                        val peer = it.peer ?: 50
                        val photo = it.photo ?: ""

                        val gender = it.member?.gender ?: ""
                        val major = it.member?.major ?: ""

                        //사진저장
                        if(photo != "") {
                            setBitmapFromBase64(photo, imgb_picture)
                        }

                        //UI에 저장()

                        et_nickname.setText(nickname)
                        et_introduce.setText(instruction)
                        tv_gender.text = gender
                        et_role.setText(role)
                        et_link.setText(personalLink)
                        tv_major.text = major

                        if(projectExperience == true)
                        {
                            spn_experience.setSelection(0)
                        }
                        else
                        {
                            spn_experience.setSelection(1)
                        }

                        var drawableResource = resources.getDrawable(R.drawable.profilecard_detail_peer0_19, null)

                        when(peer)
                        {
                            in 0..19 -> drawableResource = resources.getDrawable(R.drawable.profilecard_detail_peer0_19, null)
                            in 20..39 ->drawableResource = resources.getDrawable(R.drawable.profilecard_detail_peer20_39, null)
                            in 40..59 ->drawableResource = resources.getDrawable(R.drawable.profilecard_detail_peer40_59, null)
                            in 60..79 ->drawableResource = resources.getDrawable(R.drawable.profilecard_detail_peer60_79, null)
                            in 80..100 ->drawableResource = resources.getDrawable(R.drawable.profilecard_detail_peer80_100, null)
                        }

                        img_peer.setImageDrawable(drawableResource)

                        if(et_nickname.text.isNotEmpty())
                        {
                            et_nickname.hint = ""
                        }
                        else{
                            et_nickname.hint = "닉네임"
                        }

                        if(et_introduce.text.isNotEmpty()){
                            et_introduce.hint = ""
                        }
                        else{
                            et_introduce.hint = "나의 다짐"
                        }

                        if(et_role.text.isNotEmpty()){
                            et_role.hint=""
                        }
                        else{
                            et_role.hint="예)프론트엔드"
                        }

                    }
                }
                else {
                }
            }

            override fun onFailure(call: Call<getProfileDetailData>, t: Throwable) {

            }
        })

    }

    private fun getTechnology(token : String, account : String)
    {
        technologyIdList.clear()
        if (account == null) {
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.143.117:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(getTechnologyRequest::class.java)

        val call: Call<List<getTechnologyData>> = apiService.getTechnology("Bearer $token", account)

        call?.enqueue(object : Callback<List<getTechnologyData>> {
            override fun onResponse(call: Call<List<getTechnologyData>>, response: Response<List<getTechnologyData>>) {
                if (response.isSuccessful) {
                    val technologyDataList: List<getTechnologyData>? = response.body()
                    technologyDataList?.let {
                        for (technologyData in it) {
                            val id = technologyData.technology.id
                            val level = technologyData.level ?: 0
                            val name = technologyData.technology?.name ?: ""

                            val textView = TextView(requireContext())
                            textView.text = "$name level : $level"
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)

                            technologyIdList.add(id)

                            // 생성된 TextView를 LinearLayout에 추가
                            linear_skill.addView(textView)
                        }
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<getTechnologyData>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && data != null){
            val selectedImage : Uri? = data.data
            imgb_picture.setImageURI(selectedImage)

            imgb_picture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun disableInputFields() {
        val originalNicknameTextColor = et_nickname.currentTextColor
        val originalIntroduceTextColor = et_introduce.currentTextColor
        val originalLinkTextColor = et_link.currentTextColor
        val originalRoleTextColor = et_role.currentTextColor


        imgb_picture.isEnabled = false
        et_nickname.isEnabled = false
        et_introduce.isEnabled = false
        spn_experience.isEnabled = false
        et_role.isEnabled = false
        et_link.isEnabled = false

        et_nickname.setTextColor(originalNicknameTextColor)
        et_introduce.setTextColor(originalIntroduceTextColor)
        et_link.setTextColor(originalLinkTextColor)
        et_role.setTextColor(originalRoleTextColor)

        val selectedView = spn_experience.selectedView as? TextView
        selectedView?.let {
            it.setTextColor(Color.BLACK)
        }

        cv_bottom.isEnabled = true
    }

    private fun enableInputFields() {
        imgb_picture.isEnabled = true
        et_nickname.isEnabled = true
        et_introduce.isEnabled = true
        spn_experience.isEnabled = true
        et_role.isEnabled = true
        et_link.isEnabled = true


        cv_bottom.isEnabled = false
    }

    private fun skillAdd(){

        skillSpinner = Spinner(requireContext())
        LevelSpinner = Spinner(requireContext())

        val items = arrayOf("JAVA", "Python")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        skillSpinner.adapter = adapter
        linear_skill.addView(skillSpinner)

        val level_items = arrayOf("level1", "level2", "level3", "level4", "level5")
        val level_adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, level_items)
        level_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        LevelSpinner.adapter = level_adapter

        linear_skill.addView(LevelSpinner)
    }

    private fun skillFinish(token : String, account : String, profileId : Int) {
        var count = 0

        var skill_value = skillSpinner.selectedItem.toString()
        var level_value = LevelSpinner.selectedItem.toString()

        var technologyId : Long = 1
        var level : Int = 1

        when(skill_value)
        {
            "Python" -> {technologyId = 1}
            "JAVA" -> { technologyId = 2}
//            "C++" -> {technologyId = 3}
//            "C" -> {technologyId = 4}
//            "C#" -> {technologyId = 5}
//            "Kotlin" -> {technologyId = 6}
//            "Swift" -> {technologyId = 7}
//            "JavaScript" -> {technologyId = 8}
        }

        when(level_value)
        {
            "level1" -> { level = 1}
            "level2" -> { level = 2}
            "level3" -> { level = 3}
            "level4" -> { level = 4}
            "level5" -> { level = 5}
        }

        technologyIdList.forEach { techId ->
            if(techId == technologyId){
                count++
            }
        }

        if(count == 0) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://3.34.143.117:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(postTechnologyRequest::class.java)

            val request = postTechnologyData(level, technologyId)

            val call: Call<Boolean> = apiService.postTechnology("Bearer $token", account, request)

            call.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show();
                        linear_skill.removeAllViews()

                        getTechnology(token, account)
                    } else {
                        Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    linear_skill.removeAllViews()

                    getTechnology(token, account)
                }

            })
        }
        else
        {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://3.34.143.117:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(putTechnologyRequest::class.java)

            val request = putTechnologyData(level)

            val call : Call<Boolean> = apiService.putTechnology("Bearer $token", profileId, technologyId, request)

            call.enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show();
                        linear_skill.removeAllViews()

                        getTechnology(token, account)
                    } else {
                        Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    linear_skill.removeAllViews()

                    getTechnology(token, account)
                }

            })
        }
    }
    fun compressAndEncodeBitmap(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        // 이미지 크기를 줄이고 JPEG 형식으로 압축
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    //비율 정할 때 쓰는 것
    fun resizeBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    private fun imageButtonToBase64(imageButton: ImageButton, quality: Int = 100): String {
        val drawable = imageButton.drawable

        if (drawable is BitmapDrawable) {
            val originalBitmap = drawable.bitmap
            //val resizedBitmap = resizeBitmap(originalBitmap, 100, 100)
            return compressAndEncodeBitmap(originalBitmap)
        } else {
            return ""
        }
    }

    private fun setBitmapFromBase64(base64String: String, imageButton: ImageButton) {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        // 이미지 버튼에 디코딩된 이미지 설정
        imageButton.setImageBitmap(bitmap)
    }
}