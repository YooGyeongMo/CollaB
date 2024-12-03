package com.gmlab.team_benew.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gmlab.team_benew.R

class LogoutPopupActivity : AppCompatActivity() {

    lateinit var btn_true : Button
    lateinit var btn_false : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_logout_popup)

        btn_true = findViewById(R.id.btn_logoutPopup_true)
        btn_false = findViewById(R.id.btn_logoutPopup_false)

        btn_true.setOnClickListener {
            sendResult(true)
            finish()
        }

        btn_false.setOnClickListener {
            sendResult(false)
            finish()
        }
    }

    private fun sendResult(result: Boolean) {
        val resultIntent = Intent()
        resultIntent.putExtra("logoutResultKey", result)
        setResult(Activity.RESULT_OK, resultIntent)
    }
}