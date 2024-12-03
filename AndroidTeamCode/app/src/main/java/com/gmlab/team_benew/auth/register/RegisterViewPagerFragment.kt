package com.gmlab.team_benew.auth.register

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

class RegisterViewPagerFragment : Fragment(){

    lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_view_pager, container, false)

        viewPager = view.findViewById(R.id.vp_registerViewPager)

        // 프래그먼트 리스트
        val fragmentList = arrayListOf<Fragment>(
            Register1Fragment(),
            Register2Fragment(),
            Register3Fragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        // ViewPager2의 스와이프 동작을 막음
        viewPager.isUserInputEnabled = false

        viewPager.adapter = adapter

        return view
    }
}