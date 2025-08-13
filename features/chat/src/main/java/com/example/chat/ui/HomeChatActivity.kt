package com.example.chat.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.chat.R
import com.example.navigation.Routes
import com.example.usecase.auth.AuthUseCase
import com.example.usecase.chat.ChatUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeChatActivity : AppCompatActivity() {

    @Inject
    lateinit var authUseCase: AuthUseCase
    private lateinit var auth: FirebaseAuth
    @Inject
    lateinit var chatUseCase: ChatUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_chat)

        auth = FirebaseAuth.getInstance()  // Initialize auth here!

        findViewById<View>(R.id.ic_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(com.example.code.common.R.anim.enter_left_to_right, com.example.code.common.R.anim.exit_right_to_left)
        }
        val moreIcon: ImageView = findViewById(R.id.ic_more)
        moreIcon.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(view: View?) {
        if (view == null) return
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(com.example.code.common.R.menu.more_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                com.example.code.common.R.id.menu_item_1 -> {
                    deleteAll()
                    true
                }
                com.example.code.common.R.id.menu_item_2 -> {
                    signOut()
                    Toast.makeText(this, "Sign out", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun deleteAll() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                chatUseCase.deleteAllChats()
                Toast.makeText(
                    this@HomeChatActivity,
                    "All chats deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this@HomeChatActivity,
                    "Failed to delete chats: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signOut() {
        CoroutineScope(Dispatchers.Main).launch {
            authUseCase.signOut()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Routes.LOGIN_REGISTER))
            startActivity(intent)

            finish()
        }
    }
}