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
import com.example.ads.AdManager // Import AdManager (từ module ads)

import com.example.chat.R // R của module chat
import com.example.navigation.Routes
import com.example.usecase.auth.AuthUseCase
import com.example.usecase.chat.ChatUseCase
import com.google.android.gms.ads.AdRequest // Import AdRequest
import com.google.android.gms.ads.AdView // Import AdView
import com.google.android.gms.ads.MobileAds // Vẫn cần MobileAds.initialize nếu chưa làm ở Application class
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

    @Inject // Hilt sẽ inject AdManager vào đây
    lateinit var adManager: AdManager

    // Khai báo AdView để có thể truy cập ở các lifecycle methods
    private lateinit var bannerAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_chat)


        MobileAds.initialize(this) {}

        auth = FirebaseAuth.getInstance()

        findViewById<View>(R.id.ic_menu).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            // Sử dụng R của module core-common cho animations
            overridePendingTransition(com.example.code.common.R.anim.enter_left_to_right, com.example.code.common.R.anim.exit_right_to_left)
        }
        val moreIcon: ImageView = findViewById(R.id.ic_more)
        moreIcon.setOnClickListener {
            showPopupMenu(it)
        }

        // --- Tích hợp Quảng cáo Banner ---
        bannerAdView = findViewById(R.id.adView) // Lấy AdView từ layout XML
        val adRequestBanner = AdRequest.Builder().build()
        bannerAdView.loadAd(adRequestBanner) // Tải quảng cáo banner

        // --- Tải trước Interstitial Ad (chỉ tải, chưa hiển thị) ---
        // Gọi loadInterstitialAd từ AdManager
        adManager.loadInterstitialAd()

        // --- Xử lý sự kiện nhấn nút Back của thiết bị ---
        // Áp dụng cho Activity, sẽ bắt sự kiện back của toàn bộ Activity
        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Hiển thị quảng cáo Interstitial trước khi đóng Activity
                adManager.showInterstitialAd(this@HomeChatActivity) {
                    // Callback này được gọi sau khi quảng cáo đóng HOẶC không thể hiển thị
                    // Sau đó, chúng ta kết thúc Activity
                    isEnabled = false // Tắt callback này để tránh lặp vô hạn
                    onBackPressed() // Gọi lại onBackPressed để kết thúc Activity một cách bình thường
                }
            }
        })
    }

    // Quản lý lifecycle của AdView banner
    override fun onPause() {
        bannerAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        bannerAdView.resume()
    }

    override fun onDestroy() {
        bannerAdView.destroy() // Hủy AdView khi Activity bị hủy
        super.onDestroy()
    }


    private fun showPopupMenu(view: View?) {
        if (view == null) return
        val popupMenu = PopupMenu(this, view)
        // Sử dụng R của module core-common cho menu
        popupMenu.menuInflater.inflate(com.example.code.common.R.menu.more_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                com.example.code.common.R.id.menu_item_1 -> { // Sử dụng R của module core-common
                    deleteAll()
                    true
                }
                com.example.code.common.R.id.menu_item_2 -> { // Sử dụng R của module core-common
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