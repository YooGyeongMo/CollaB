package com.gmlab.team_benew.main.home.firstSetting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.gmlab.team_benew.test.CodingTestActivity
import com.gmlab.team_benew.R

class FirstSetting3Fragment : Fragment() {

    lateinit var btn_goTest : Button
    lateinit var btn_close : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_setting3, container, false)

        btn_close = view.findViewById(R.id.btn_firstSetting3_close)
        btn_goTest = view.findViewById(R.id.btn_firstSetting3_test)

        btn_goTest.setOnClickListener {
            val intent = Intent(requireContext(), CodingTestActivity::class.java)
            startActivity(intent)

            activity?.finish()
        }

        btn_close.setOnClickListener {
            activity?.finish()
        }

        return view
    }
}