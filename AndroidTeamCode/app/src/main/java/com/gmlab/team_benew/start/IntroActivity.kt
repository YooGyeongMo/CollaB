package com.gmlab.team_benew.start

import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.LoginActivity
import com.gmlab.team_benew.auth.register.RegisterActivity
import com.gmlab.team_benew.databinding.ActivityIntroBinding
import com.gmlab.team_benew.util.getStatusBarHeight
import com.google.android.material.tabs.TabLayout


class IntroActivity:AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: IntroBannerAdapter
    private val handler = Handler()
    private lateinit var binding: ActivityIntroBinding
    private lateinit var fadeInAnim: Animation
    private lateinit var fadeInAnimBtn: Animation



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.introRootLayout.setPadding(0, getStatusBarHeight(this), 0, 0)

        //status bar와 navigation bar 모두 투명하게 만드는 코드
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // ProgressBar 로딩 중
        binding.pgIntroProgressBar.visibility = View.VISIBLE



        // GIF 로드 및 재생
        val imageView = findViewById<ImageView>(R.id.iv_intro_gif)
        Glide.with(this)
            .asGif()
            .load("file:///android_asset/collab_intro_2.gif")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object
                : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    // 로드 실패 시 ProgressBar 숨김
                    binding.pgIntroProgressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable,
                    model: Any,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // GIF 로드 완료 시 ProgressBar 숨김
                    binding.pgIntroProgressBar.visibility = View.GONE
                    resource?.start()

                    // 2초 후에 버튼 표시
                    handler.postDelayed({
                        showButtonsWithAnimation()
                    }, 2000)

                    // 5초 후에 GIF를 멈추고 버튼 표시
                    handler.postDelayed({
                        resource?.stop()
                    }, 10000)

                    return false
                }
            })
            .into(imageView)


        binding.btnIntroLogin.visibility = View.GONE
        binding.btnIntroRegister.visibility = View.GONE

        fadeInAnimBtn = AnimationUtils.loadAnimation(this, R.anim.fadee_in)

        // 버튼 클릭 리스너 설정
        binding.btnIntroLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnIntroRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 완료 후 알림 확인
        checkAndShowAlert()
    }

    private fun showButtonsWithAnimation() {
        binding.btnIntroLogin.visibility = View.VISIBLE
        binding.btnIntroRegister.visibility = View.VISIBLE

        binding.btnIntroLogin.startAnimation(fadeInAnimBtn)
        binding.btnIntroRegister.startAnimation(fadeInAnimBtn)
    }

    private fun checkAndShowAlert() {
        val showAlert = intent.getBooleanExtra("showAlert", false)

        if (showAlert) {
            // AlertDialog 표시
            AlertDialog.Builder(this).apply {
                setTitle("회원가입 성공")
                setMessage("회원가입이 성공적으로 되었습니다.")
                setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }

            // 플래그를 초기화하여 다이얼로그가 반복적으로 표시되지 않도록 함
            intent.removeExtra("showAlert")
        }
    }
}