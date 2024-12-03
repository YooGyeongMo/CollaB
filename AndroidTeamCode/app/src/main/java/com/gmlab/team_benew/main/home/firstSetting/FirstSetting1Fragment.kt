package com.gmlab.team_benew.main.home.firstSetting

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.getRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class FirstSetting1Fragment : Fragment() {

    lateinit var imgb_picture : ImageButton
    lateinit var et_nickname : EditText
    lateinit var tv_nickname : TextView
    lateinit var btn_next : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_setting1, container, false)

        imgb_picture = view.findViewById(R.id.imgb_firstSetting1_photo)
        et_nickname = view.findViewById(R.id.et_firstSetting1_nickname)
        tv_nickname = view.findViewById(R.id.tv_firstSetting1_nickname)
        btn_next = view.findViewById(R.id.btn_firstSetting1_next)

        imgb_picture.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }

        btn_next.setOnClickListener {
            next()
        }

        return view
    }

    //확인버튼
    private fun next(){
        if(et_nickname.text.isEmpty()){
            tv_nickname.text = "*닉네임 - 필수 작성 항목입니다."
            tv_nickname.setTextColor(Color.parseColor("#D1180B"))
            return
        }

        //밑 3줄 테스트용 코드 삭제할 것
        //val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp_firstSettingViewPager_viewPager)
        //val currentIndex = viewPager.currentItem
        //viewPager.setCurrentItem(currentIndex + 1, true)

        var nickname = et_nickname.text.toString()
        var photo = imageButtonToBase64(imgb_picture) ?: ""

        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        val token = sharedPref?.getString("userToken", "")
        val memberId = sharedPref?.getInt("loginId", 0)



        if (!token.isNullOrEmpty() && memberId != null && memberId != 0) {
            val apiService = getRetrofit().create(postFirstSettingRequest::class.java)
            val request = postFirstSettingData(nickname, photo)
            val call: Call<ResponseBody> = apiService.postFirstSetting("Bearer $token", memberId, request)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(requireContext(), "저장했습니다", Toast.LENGTH_LONG).show()

                        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp_firstSettingViewPager_viewPager)
                        val currentIndex = viewPager.currentItem

                        // 다음 화면으로 이동
                        viewPager.setCurrentItem(currentIndex + 1, true)
                    } else{
                        Toast.makeText(requireContext(), "${response.code()}저장 실패", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("API_CALL_FAILURE", "API Call Failed", t)
                }

            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && data != null){
            val selectedImage : Uri? = data.data
            imgb_picture.setImageURI(selectedImage)

            imgb_picture.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    fun compressAndEncodeBitmap(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun imageButtonToBase64(imageButton: ImageButton): String? {
        val drawable = imageButton.drawable

        if (drawable is BitmapDrawable) {
            val originalBitmap = drawable.bitmap
            return compressAndEncodeBitmap(originalBitmap)
        } else {
            return null
        }
    }

}