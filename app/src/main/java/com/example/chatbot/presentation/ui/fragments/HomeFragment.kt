package com.example.chatbot.presentation.ui.fragments

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
import com.example.chatbot.R
import com.example.chatbot.databinding.FragmentHomeBinding
import com.example.chatbot.presentation.adapters.DayChatHistoryAdapter
import com.example.chatbot.presentation.utils.Resource
import com.example.chatbot.presentation.utils.getNavOptions
import com.example.chatbot.presentation.viewmodels.HomeViewModel
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
                R.id.action_homeFragment_to_chatFragment,
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
            findNavController().navigate(R.id.action_homeFragment_to_chatFragment, null, getNavOptions())
        }
        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allChatFragment)
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
                            R.id.action_homeFragment_to_chatFragment,
                            Bundle().apply {
                                putString("chatId", chatId)
                            },  getNavOptions())
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