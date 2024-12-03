package com.gmlab.team_benew.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gmlab.team_benew.R

class TestFragment:  Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_ai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedLanguage = arguments?.getString("selectedLanguage")

        val tvLangName = view.findViewById<TextView>(R.id.tv_welcome_testing_lang_name)

        tvLangName.text = selectedLanguage ?: "언어가 선택되지 않았습니다"
    }
}