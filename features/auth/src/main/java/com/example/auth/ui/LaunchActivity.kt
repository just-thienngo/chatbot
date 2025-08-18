package com.example.auth.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.auth.R
import com.example.auth.databinding.ActivityLaunchBinding
import com.example.code.common.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LaunchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLaunchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.retryButton.setOnClickListener {
            checkNetworkAndProceed()
        }

        checkNetworkAndProceed()

    }

    private fun checkNetworkAndProceed() {

        binding.noInternetLayout.visibility = View.GONE
        binding.logo.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        if (NetworkUtils.isNetworkAvailable(this)) {
            lifecycleScope.launch {
                delay(1000)
                val intent = Intent(this@LaunchActivity, LoginRegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {

            binding.logo.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.noInternetLayout.visibility = View.VISIBLE
            Toast.makeText(this, "No internet connect, Please check again setting.", Toast.LENGTH_LONG).show()
        }

    }

}