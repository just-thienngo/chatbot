package com.example.chatbot.presentation.ui.fragments.lognRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chatbot.R
import com.example.chatbot.databinding.FragmentIntroductionBinding
import com.example.chatbot.presentation.ui.activities.HomeChatActivity
import com.example.chatbot.presentation.viewmodels.IntroductionViewModel
import com.example.chatbot.presentation.viewmodels.IntroductionViewModel.Companion.HOMECHAT_ACTIVITY
import com.example.chatbot.presentation.viewmodels.IntroductionViewModel.Companion.LOGIN_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
 private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {
    viewModel.navigate.collect {
    when (it) {
        HOMECHAT_ACTIVITY -> {
            Intent(requireActivity(), HomeChatActivity::class.java).also { intent ->
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        LOGIN_FRAGMENT -> {
            findNavController().navigate(it)
        }
        else -> Unit
    }
}
        }

    binding.lnGetStarted.setOnClickListener {
        viewModel.startButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_loginFragment)
        }
    }


}