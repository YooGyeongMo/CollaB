package com.gmlab.team_benew.main.home.firstSetting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class FirstSetting2Fragment : Fragment() {

    lateinit var btn_next : Button
    lateinit var chipGroup : ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_setting2, container, false)

        btn_next = view.findViewById(R.id.btn_firstSetting2_next)
        chipGroup = view.findViewById(R.id.chipg_firstSetting2_chipGroup)

        btn_next.setOnClickListener {
            next()
        }

        return view
    }

    private fun next(){
        val selectedChipTexts = mutableListOf<String>()

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedChipTexts.add(chip.text.toString())
            }
        }

        if(selectedChipTexts.size < 3){
            Toast.makeText(requireContext(), "3개 이상 선택해 주세요.", Toast.LENGTH_LONG).show()

            return
        }

        //선택된 칩의 텍스트들을 sharedPreference에 저장
        val sharedPrefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putStringSet("Interest", selectedChipTexts.toSet())
        editor.apply()


        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp_firstSettingViewPager_viewPager)
        val currentIndex = viewPager.currentItem

        // 다음 화면으로 이동
        viewPager.setCurrentItem(currentIndex + 1, true)
    }
}