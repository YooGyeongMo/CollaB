package com.gmlab.team_benew.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.AuthResponse
import com.gmlab.team_benew.auth.AuthRetrofitInterface
import com.gmlab.team_benew.auth.AuthService
import com.gmlab.team_benew.auth.LoginActivity
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.databinding.ActivitySplashBinding
import com.gmlab.team_benew.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gmlab.team_benew.util.getStatusBarHeight

class SplashActivity: AppCompatActivity(),SplashView{
    private val handler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashRootLayout.setPadding(0, getStatusBarHeight(this), 0, 0)

        //status bar와 navigation bar 모두 투명하게 만드는 코드
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // 프로그레스 바 초기 숨김
        binding.pgProgressBar.visibility = View.GONE


//        // 여러 개의 애니메이션을 담을 AnimationSet 생성
//        val animationSet = AnimationSet(true)
//        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
//        // AnimationSet에 애니메이션 추가
//        animationSet.addAnimation(fadeInAnim)
//
//        // 애니메이션을 시작할 뷰에 AnimationSet 적용
////        binding.ivSplashBg.startAnimation(animationSet)
//        binding.ivSplashTitle.startAnimation(animationSet)
//        binding.ivSplashTitleTxt.startAnimation(animationSet)

        val imageView = findViewById<ImageView>(R.id.iv_splash_title)
        Glide.with(this)
            .asGif()
            .load("file:///android_asset/collab_splash_logo.gif")
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시 전략 설정
            .into(imageView)

        handler.postDelayed({
            val token = getTokenFromSharedPreferences(this)
            val account = getAccountFromSharedPreferences(this)

            if (token != null && account != null) {
                // 토큰 유무에 따라 확인
                binding.pgProgressBar.visibility = View.VISIBLE // 프로그레스 바 보이기

                val splashAuthService = SplashAuthService(this)
                splashAuthService.setSplashView(this)
                splashAuthService.verifyUserToken(token, account)
            } else {
                // token이 null이면 바로 로그인 화면으로 이동
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }
        }, 4000) // 애니메이션 종료 후 3초 뒤에 토큰 체크
    }


    // SharedPreferences에서 token값을 가져오는 함수
    private fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)

    }

    private fun getAccountFromSharedPreferences(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userAccount", null)
    }

    override fun onTokenCheckSuccess() {
        Log.d("TokenCheck/SUCCESS", "토큰이 유효합니다")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onTokenCheckFailure() {
        Toast.makeText(this, "인증이 되지 않은 사용자입니다. 401 에러입니다.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, IntroActivity::class.java))
        //토큰 유효하지 않을 시 sharedpreferences 새로운 토큰으로 패치
        finish()
    }

    override fun onTokenCheckElseFailure() {
        Toast.makeText(this, "알 수 없는 에러입니다.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, IntroActivity::class.java))
        //토큰 유효하지 않을 시 sharedpreferences 새로운 토큰으로 패치
        finish()
    }


}