package com.gmlab.team_benew.auth.register

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.register.screens.Register1Fragment

lateinit var progress : View

class RegisterActivity : AppCompatActivity() {

    lateinit var registerViewModel: RegisterViewModel

    companion object {
        private var instance: RegisterActivity? = null

        // RegisterActivity의 인스턴스를 반환하는 정적 메소드
        fun getInstance(): RegisterActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // RegisterActivity의 인스턴스를 할당
        instance = this

        progress = findViewById(R.id.view_register_progress)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //RegisterActivity가 종료될 때 인스턴스를 null로 설정합니다.
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    override fun onBackPressed() {
        val viewPager = findViewById<ViewPager2>(R.id.vp_registerViewPager)
        val currentIndex = viewPager.currentItem

        // 현재 Fragment가 FirstRegisterFragment일 때만 처리
        val currentFragment = supportFragmentManager.fragments[currentIndex]
        if (currentFragment is Register1Fragment) {
            // 현재 페이지가 첫 번째 페이지이고 더 이상 뒤로 갈 수 없는 경우, 기본 동작을 수행
            if (currentIndex == 0) {
                super.onBackPressed()
            } else {
                progress_decrease()
                viewPager.setCurrentItem(currentIndex - 1, true)
            }
        } else {
            // 현재 Fragment가 FirstRegisterFragment가 아닌 경우 액티비티 종료
            finish()
        }
    }

    fun progress_increase() {
        val layoutParams = progress.layoutParams as? ConstraintLayout.LayoutParams
        layoutParams?.let {
            // 20% 증가
            val increasedWidthPercent = 0.3f

            // 기존 비율 값을 가져옵니다.
            val currentWidthPercent = it.matchConstraintPercentWidth

            // 비율을 증가시킵니다.
            val newWidthPercent = currentWidthPercent + increasedWidthPercent

            // 최대 값이 1이므로, 1을 초과하지 않도록 합니다.
            it.matchConstraintPercentWidth = newWidthPercent.coerceAtMost(1.0f)

            // 레이아웃을 다시 그리도록 요청합니다.
            progress.requestLayout()
        }
    }

    fun progress_decrease() {
        val layoutParams = progress.layoutParams as? ConstraintLayout.LayoutParams
        layoutParams?.let {
            // 20% 감소
            val increasedWidthPercent = -0.3f

            // 기존 비율 값을 가져옵니다.
            val currentWidthPercent = it.matchConstraintPercentWidth

            // 비율을 감소시킵니다.
            val newWidthPercent = currentWidthPercent + increasedWidthPercent

            // 최대 값이 1이므로, 1을 초과하지 않도록 합니다.
            it.matchConstraintPercentWidth = newWidthPercent.coerceAtMost(1.0f)

            // 레이아웃을 다시 그리도록 요청합니다.
            progress.requestLayout()
        }
    }
}