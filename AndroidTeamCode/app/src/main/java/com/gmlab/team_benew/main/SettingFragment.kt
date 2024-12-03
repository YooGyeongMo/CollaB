package com.gmlab.team_benew.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gmlab.team_benew.R
import com.gmlab.team_benew.start.IntroActivity

class SettingFragment: Fragment() {

    lateinit var btn_logout : Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        btn_logout = view.findViewById(R.id.btn_setting_logout)

        btn_logout.setOnClickListener {
            logout()
        }

        return view
    }

    private fun logout() {
        val intent = Intent(requireContext(), LogoutPopupActivity::class.java)

        startActivityForResult(intent, LOGOUT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGOUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val logoutResult = data?.getBooleanExtra("logoutResultKey", false) ?: false
            handleLogoutResult(logoutResult)
        }
    }

    private fun handleLogoutResult(result: Boolean) {
        if (result) {
            // 로그아웃 수행
            Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            sharedPreferences?.let { // SharedPreferences가 null이 아닐 때만 코드 실행
                val editor = it.edit()
                editor.clear() // 모든 데이터를 지움
                editor.apply() // 변경 사항을 적용

                val intent = Intent(requireContext(), IntroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 새로운 태스크 시작 후 이전 태스크 종료
                startActivity(intent)

                requireActivity().finish()
            }
        }
    }

    companion object {
        const val LOGOUT_REQUEST_CODE = 1
    }
}