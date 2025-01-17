package com.example.chatbot.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
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
        val moreIcon: ImageView = findViewById(R.id.ic_more)
        moreIcon.setOnClickListener {
            showPopupMenu(it) // 'it' is the clicked view
        }
    }

    private fun showPopupMenu(view: View?) { // Pass View? as a parameter
        if(view == null) return
        val popupMenu = PopupMenu(this, view) // Use passed view parameter
        popupMenu.menuInflater.inflate(R.menu.more_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item_1 -> {

                    Toast.makeText(this, "Item 1 clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_item_2 -> {

                    Toast.makeText(this, "Item 2 clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
}