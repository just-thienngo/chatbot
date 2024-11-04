package com.example.chatbot.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot.R

class LaunchActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launch)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}