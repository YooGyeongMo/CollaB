package com.gmlab.team_benew.main.home.firstSetting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.register.RegisterActivity
import com.gmlab.team_benew.auth.register.screens.Register1Fragment

class FirstSettingActivity : AppCompatActivity() {

    companion object {
        private var instance: FirstSettingActivity? = null

        // RegisterActivity의 인스턴스를 반환하는 정적 메소드
        fun getInstance(): FirstSettingActivity? {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_setting)

        instance = this

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onBackPressed() {
        val viewPager = findViewById<ViewPager2>(R.id.vp_firstSettingViewPager_viewPager)
        val currentIndex = viewPager.currentItem

        // 현재 Fragment가 FirstRegisterFragment일 때만 처리
        val currentFragment = supportFragmentManager.fragments[currentIndex]
        if (currentFragment is FirstSetting1Fragment) {
            // 현재 페이지가 첫 번째 페이지이고 더 이상 뒤로 갈 수 없는 경우, 기본 동작을 수행
            if (currentIndex == 0) {
                super.onBackPressed()
            } else {
                viewPager.setCurrentItem(currentIndex - 1, true)
            }
        } else {

        }
    }

    //Activity가 종료될 때 인스턴스를 null로 설정합니다.
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}