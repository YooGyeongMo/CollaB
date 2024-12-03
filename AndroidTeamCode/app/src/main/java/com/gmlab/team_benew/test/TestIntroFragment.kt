package com.gmlab.team_benew.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmlab.team_benew.R

class TestIntroFragment: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test_intro, container, false)

        val btnJava = view.findViewById<Button>(R.id.gbtn_java)
        btnJava.setOnClickListener {
            val bundle = Bundle().apply {
                putString("selectedLanguage", "자바")
            }
            findNavController().navigate(R.id.action_intro_testing_to_testing, bundle)
        }

        val btnPython = view.findViewById<Button>(R.id.gbtn_python)
        btnPython.setOnClickListener {
            val bundle = Bundle().apply {
                putString("selectedLanguage", "파이썬")
            }
            findNavController().navigate(R.id.action_intro_testing_to_testing, bundle)
        }

        return view
    }

}