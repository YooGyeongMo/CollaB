package com.gmlab.team_benew.main.home.firstSetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.register.screens.Register1Fragment
import com.gmlab.team_benew.auth.register.screens.Register2Fragment
import com.gmlab.team_benew.auth.register.screens.Register3Fragment
import com.gmlab.team_benew.start.ViewPagerAdapter

class FirstSettingViewPagerFragment : Fragment() {

    lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_setting_view_pager, container, false)

        viewPager = view.findViewById(R.id.vp_firstSettingViewPager_viewPager)

        // 프래그먼트 리스트
        val fragmentList = arrayListOf<Fragment>(
            //프래그먼트 채우기
            FirstSetting1Fragment(),
            FirstSetting2Fragment(),
            FirstSetting3Fragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.isUserInputEnabled = false

        viewPager.adapter = adapter

        return view
    }

}