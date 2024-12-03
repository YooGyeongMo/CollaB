package com.gmlab.team_benew.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gmlab.team_benew.main.MainActivity
import com.gmlab.team_benew.R
import com.gmlab.team_benew.databinding.ActivityLoginBinding
import com.gmlab.team_benew.util.getStatusBarHeight
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Response

// 다음 페이지에서 로그인 성공시 User Account 값 공유가능한 글로벌 데이터에 담아서 소통
// ID 값 응답값에서 담아온거 저장하기

class LoginActivity : AppCompatActivity(), LoginView, ReLoginView {
    private lateinit var binding: ActivityLoginBinding
    private val authService = AuthService()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginRootLayout.setPadding(0, getStatusBarHeight(this), 0, 0)

        //status bar와 navigation bar 모두 투명하게 만드는 코드
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        textWatcher()

        //은닉화 및 캡슐화
        binding.btnLoginLogin.setOnClickListener{

            Login()
            //임시방편
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

//             필요한 경우 현재 액티비티를 종료
//            finish()
        }

        // 뒤로가기 버튼 클릭 리스너 설정
        binding.loginBacktab.setOnClickListener {
            onBackPressed() // 뒤로 가기 버튼 클릭 시 액티비티 종료
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        // 뒤로 가기 버튼 클릭 시 추가로 필요한 동작이 있다면 여기서 처리
        // 예: Toast 메시지 표시
        // Toast.makeText(this, "뒤로 가기 버튼 클릭", Toast.LENGTH_SHORT).show()
    }


    private fun getUser() : User {
        val id : String = binding.tetLoginId.text.toString()
        val pwd : String = binding.tetLoginPw.text.toString()

        return User(id, pwd)
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun Login() {

            if(binding.tetLoginId.text.toString().isEmpty()) {
                Toast.makeText(this, "아이디가 비어있습니다" , Toast.LENGTH_SHORT).show()
                return
            }
            if(binding.tetLoginPw.text.toString().isEmpty()){
                Toast.makeText(this, "비밀번호가 비어있습니다.",Toast.LENGTH_SHORT).show()
                return
            }

            val authService = AuthService()
            authService.setLoginView(this)

            authService.login(getUser(),this)

    }

    private fun textWatcher() {
        val loginId = binding.tetLoginId
        val tiLoginId = binding.tiLoginId

        val passwordEditText = binding.tetLoginPw
        val passwordTextInputLayout = binding.tiLoginPw

        loginId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (loginId.text!!.isEmpty()) {
                    tiLoginId.error = "아이디를 입력해주세요"
                } else {
                    tiLoginId.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (passwordEditText.text!!.isEmpty()) {
                    passwordTextInputLayout.error = "비밀번호를 입력해주세요"
                } else {
                    passwordTextInputLayout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


        })
    }

    override fun onLoginSuccess() {
        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
        finish()
        startMainActivity()
    }

    override fun onLoginFailure() {
        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
        Log.d("LOGIN/FAILURE","로그인 오류")
    }

    override fun onReLoginSuccess() {
        Log.d("RELOGIN/SUCCESS","로그인 성공")
        finish()
        startMainActivity()
    }

    override fun onReLoginFailure() {
        Log.d("RELOGIN/FAILURE","재로그인 오류")
    }

    override fun onAuthenticationFailure() {
        Toast.makeText(
            this,
            "아이디 또는 비밀번호를 잘못 입력했습니다.\n입력한 내용을 다시 확인해주세요",
            Toast.LENGTH_SHORT
        ).show()
    }

}