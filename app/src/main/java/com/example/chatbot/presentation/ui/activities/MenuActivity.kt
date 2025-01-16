package com.example.chatbot.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MenuActivity: AppCompatActivity(R.layout.activity_menu) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
    }
}