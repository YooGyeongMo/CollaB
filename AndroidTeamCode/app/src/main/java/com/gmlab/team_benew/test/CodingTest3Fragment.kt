package com.gmlab.team_benew.test

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.gmlab.team_benew.R

class CodingTest3Fragment : Fragment() {

    lateinit var btn_next : Button
    lateinit var tv_score : TextView

    lateinit var codingTestViewModel : CodingTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coding_test3, container, false)

        btn_next = view.findViewById(R.id.btn_codingTest3_next)
        tv_score = view.findViewById(R.id.tv_codingTest3_score)

        codingTestViewModel = ViewModelProvider(requireActivity()).get(CodingTestViewModel::class.java)

        tv_score.text =  codingTestViewModel.r_score.toString()

        btn_next.setOnClickListener {

            //셰어드프리퍼런스에 저장 key : TestScore
            val sharedPrefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putInt("TestScore", codingTestViewModel.r_score)
            editor.apply()

            activity?.finish()
        }

        return view
    }

}