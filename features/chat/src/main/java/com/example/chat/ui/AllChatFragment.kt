package com.example.chat.ui

import ChatItemAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.databinding.FragmentAllChatBinding
import com.example.chat.viewmodels.AllChatViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllChatFragment:Fragment(R.layout.fragment_all_chat) {
    private lateinit var binding: FragmentAllChatBinding
    private val viewModel: AllChatViewModel by viewModels()
    private lateinit var chatItemAdapter: ChatItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllChatBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        chatItemAdapter = ChatItemAdapter(emptyList(), onDeleteChat = {
            // handle delete chat here
        }, onChatClick = { chatId ->
            findNavController().navigate(
                com.example.navigation.R.id.action_allChatFragment_to_chatFragment,
                Bundle().apply {
                    putString("chatId", chatId)
                })
        })
        binding.rcvAllChat.apply {
            adapter = chatItemAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
    private fun observeViewModel() {
        viewModel.chats.observe(viewLifecycleOwner){ chats ->
            Log.d("AllChatFragment", "observeViewModel: chats=$chats")
            chatItemAdapter = ChatItemAdapter(chats, onDeleteChat = {
                // handle delete chat here
            }, onChatClick = { chatId ->
                findNavController().navigate(
                    com.example.navigation.R.id.action_allChatFragment_to_chatFragment,
                    Bundle().apply {
                        putString("chatId", chatId)
                    })
            })
            binding.rcvAllChat.adapter = chatItemAdapter
        }
    }
}