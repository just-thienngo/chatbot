package com.example.chat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.adapter.DayChatHistoryAdapter
import com.example.chat.databinding.FragmentHomeBinding
import com.example.chat.viewmodels.HomeViewModel
import com.example.code.common.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

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
        }, onChatClick = { chatId ->
            findNavController().navigate(
                com.example.navigation.R.id.action_homeFragment_to_chatFragment,
                Bundle().apply {
                    putString("chatId", chatId)
                })
        })
        binding.rcvTimechat.apply {
            adapter = dayChatHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupClickListeners() {
        binding.lnCreateNewChats.setOnClickListener {
            findNavController().navigate(com.example.navigation.R.id.action_homeFragment_to_chatFragment)
        }
        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(com.example.navigation.R.id.action_homeFragment_to_allChatFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.chatHistory.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("HomeFragment", "observeViewModel: loading")
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    Log.d("HomeFragment", "observeViewModel: chats=${result.data}")
                    binding.progressBar.visibility = View.GONE
                    val sortedChats = (result.data ?: emptyList()).sortedByDescending {
                        it.chats.maxByOrNull { chat ->
                            chat.timestamp?.time ?: 0
                        }?.timestamp
                    }
                    dayChatHistoryAdapter = DayChatHistoryAdapter(sortedChats, onDeleteChat = { chatId ->
                        viewModel.deleteChat(chatId)
                    }, onChatClick = { chatId ->
                        findNavController().navigate(
                            com.example.navigation.R.id.action_homeFragment_to_chatFragment,
                            Bundle().apply {
                                putString("chatId", chatId)
                            })
                    })
                    binding.rcvTimechat.adapter = dayChatHistoryAdapter
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }
}