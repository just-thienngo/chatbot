package com.example.chatbot.presentation.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbot.R
import com.example.chatbot.databinding.ActivityMenuBinding
import com.example.chatbot.presentation.adapters.DayChatHistoryAdapterForMenu
import com.example.chatbot.presentation.utils.Resource
import com.example.chatbot.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity: AppCompatActivity(R.layout.activity_menu) {
    private lateinit var binding: ActivityMenuBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var dayChatHistoryAdapter: DayChatHistoryAdapterForMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        observeViewModel()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left)
    }
    private fun setupRecyclerView() {
        dayChatHistoryAdapter = DayChatHistoryAdapterForMenu(emptyList())
        binding.rcvTimechat1.apply {
            adapter = dayChatHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    private fun observeViewModel() {
        viewModel.chatHistory.observe(this){ result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("MenuActivity", "observeViewModel: loading")
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    Log.d("MenuActivity", "observeViewModel: chats=${result.data}")
                    binding.progressBar.visibility = View.GONE
                    val sortedChats = (result.data ?: emptyList()).sortedByDescending {
                        it.date
                    }
                    dayChatHistoryAdapter = DayChatHistoryAdapterForMenu(sortedChats)
                        // handle delete chat here)
                    binding.rcvTimechat1.adapter = dayChatHistoryAdapter
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }
}