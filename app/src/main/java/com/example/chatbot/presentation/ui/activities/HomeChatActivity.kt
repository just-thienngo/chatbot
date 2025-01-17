package com.example.chatbot.presentation.ui.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_chat)

        findViewById<View>(R.id.ic_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left)
        }
    }
}
