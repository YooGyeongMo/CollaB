package com.gmlab.team_benew.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.register.RegisterViewModel

class CodingTest2Fragment : Fragment() {

    lateinit var rg_quiz1 : RadioGroup
    lateinit var rg_quiz2 : RadioGroup
    lateinit var rg_quiz3 : RadioGroup
    lateinit var rg_quiz4 : RadioGroup
    lateinit var rg_quiz5 : RadioGroup
    lateinit var rg_quiz6 : RadioGroup
    lateinit var rg_quiz7 : RadioGroup
    lateinit var rg_quiz8 : RadioGroup
    lateinit var rg_quiz9 : RadioGroup
    lateinit var rg_quiz10 : RadioGroup
    lateinit var rg_quiz11 : RadioGroup
    lateinit var rg_quiz12 : RadioGroup
    lateinit var rg_quiz13 : RadioGroup
    lateinit var rg_quiz14 : RadioGroup
    lateinit var rg_quiz15 : RadioGroup

    lateinit var btn_next : Button

    lateinit var codingTestViewModel : CodingTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coding_test2, container, false)

        rg_quiz1 = view.findViewById(R.id.rag_codingTest3_1)
        rg_quiz2 = view.findViewById(R.id.rag_codingTest3_2)
        rg_quiz3 = view.findViewById(R.id.rag_codingTest3_3)
        rg_quiz4 = view.findViewById(R.id.rag_codingTest3_4)
        rg_quiz5 = view.findViewById(R.id.rag_codingTest3_5)
        rg_quiz6 = view.findViewById(R.id.rag_codingTest3_6)
        rg_quiz7 = view.findViewById(R.id.rag_codingTest3_7)
        rg_quiz8 = view.findViewById(R.id.rag_codingTest3_8)
        rg_quiz9 = view.findViewById(R.id.rag_codingTest3_9)
        rg_quiz10 = view.findViewById(R.id.rag_codingTest3_10)
        rg_quiz11 = view.findViewById(R.id.rag_codingTest3_11)
        rg_quiz12 = view.findViewById(R.id.rag_codingTest3_12)
        rg_quiz13 = view.findViewById(R.id.rag_codingTest3_13)
        rg_quiz14 = view.findViewById(R.id.rag_codingTest3_14)
        rg_quiz15 = view.findViewById(R.id.rag_codingTest3_15)

        btn_next = view.findViewById(R.id.btn_codingTest2_next)

        codingTestViewModel = ViewModelProvider(requireActivity()).get(CodingTestViewModel::class.java)
        codingTestViewModel.r_score = 0

        btn_next.setOnClickListener {
            next()
        }

        return view
    }

    private fun next(){
        var score = 0

        val selectedIndex1 = rg_quiz1.indexOfChild(view?.findViewById(rg_quiz1.checkedRadioButtonId))
        val selectedIndex2 = rg_quiz2.indexOfChild(view?.findViewById(rg_quiz2.checkedRadioButtonId))
        val selectedIndex3 = rg_quiz3.indexOfChild(view?.findViewById(rg_quiz3.checkedRadioButtonId))
        val selectedIndex4 = rg_quiz4.indexOfChild(view?.findViewById(rg_quiz4.checkedRadioButtonId))
        val selectedIndex5 = rg_quiz5.indexOfChild(view?.findViewById(rg_quiz5.checkedRadioButtonId))
        val selectedIndex6 = rg_quiz6.indexOfChild(view?.findViewById(rg_quiz6.checkedRadioButtonId))
        val selectedIndex7 = rg_quiz7.indexOfChild(view?.findViewById(rg_quiz7.checkedRadioButtonId))
        val selectedIndex8 = rg_quiz8.indexOfChild(view?.findViewById(rg_quiz8.checkedRadioButtonId))
        val selectedIndex9 = rg_quiz9.indexOfChild(view?.findViewById(rg_quiz9.checkedRadioButtonId))
        val selectedIndex10 = rg_quiz10.indexOfChild(view?.findViewById(rg_quiz10.checkedRadioButtonId))
        val selectedIndex11 = rg_quiz11.indexOfChild(view?.findViewById(rg_quiz11.checkedRadioButtonId))
        val selectedIndex12 = rg_quiz12.indexOfChild(view?.findViewById(rg_quiz12.checkedRadioButtonId))
        val selectedIndex13 = rg_quiz13.indexOfChild(view?.findViewById(rg_quiz13.checkedRadioButtonId))
        val selectedIndex14 = rg_quiz14.indexOfChild(view?.findViewById(rg_quiz14.checkedRadioButtonId))
        val selectedIndex15 = rg_quiz15.indexOfChild(view?.findViewById(rg_quiz15.checkedRadioButtonId))

        if(selectedIndex1 == 0){
            score++
        }
        if(selectedIndex2 == 2){
            score++
        }
        if(selectedIndex3 == 1){
            score++
        }
        if(selectedIndex4 == 2){
            score++
        }
        if(selectedIndex5 == 1){
            score++
        }
        if(selectedIndex6 == 2){
            score++
        }
        if(selectedIndex7 == 0){
            score++
        }
        if(selectedIndex8 == 2){
            score++
        }
        if(selectedIndex9 == 0){
            score++
        }
        if(selectedIndex10 == 1){
            score++
        }
        if(selectedIndex11 == 1){
            score++
        }
        if(selectedIndex12 == 2){
            score++
        }
        if(selectedIndex13 == 2){
            score++
        }
        if(selectedIndex14 == 2){
            score++
        }
        if(selectedIndex15 == 1){
            score++
        }

        codingTestViewModel.r_score = score

        val transaction = parentFragmentManager.beginTransaction()
        val nextFragment = CodingTest3Fragment()
        transaction.replace(R.id.fragc_codingTest_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}