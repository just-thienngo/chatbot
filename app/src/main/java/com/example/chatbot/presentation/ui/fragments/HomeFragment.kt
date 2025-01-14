package com.example.chatbot.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbot.R
import com.example.chatbot.databinding.FragmentHomeBinding
import com.example.chatbot.presentation.adapters.DayChatHistoryAdapter
import com.example.chatbot.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var dayChatHistoryAdapter: DayChatHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        dayChatHistoryAdapter = DayChatHistoryAdapter(emptyList(), onDeleteChat = {
            // we will implement deleteChat method
        })
        binding.rcvTimechat.apply {
            adapter = dayChatHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupClickListeners() {
        binding.lnCreateNewChats.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_chatFragment)
        }
    }
    private fun observeViewModel() {
        viewModel.chatHistory.observe(viewLifecycleOwner){ chatHistory ->
            Log.d("HomeFragment", "observeViewModel: chats=$chatHistory")
            val sortedChats = chatHistory.sortedByDescending {
                it.chats.maxByOrNull { chat ->
                    chat.timestamp?.time ?: 0
                }?.timestamp
            }
            dayChatHistoryAdapter = DayChatHistoryAdapter(sortedChats, onDeleteChat = { chatId ->
                viewModel.deleteChat(chatId)
            })
            binding.rcvTimechat.adapter = dayChatHistoryAdapter
        }
    }
}