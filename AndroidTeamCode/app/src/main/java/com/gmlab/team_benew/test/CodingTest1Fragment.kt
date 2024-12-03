package com.gmlab.team_benew.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.gmlab.team_benew.R

class CodingTest1Fragment : Fragment() {

    lateinit var btn_next : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coding_test1, container, false)

        btn_next = view.findViewById(R.id.btn_codingTest1_next)

        btn_next.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()

            val nextFragment = CodingTest2Fragment()

            transaction.replace(R.id.fragc_codingTest_container, nextFragment)

            transaction.addToBackStack(null)

            transaction.commit()
        }

        return view
    }

}