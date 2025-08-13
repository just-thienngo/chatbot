    package com.example.auth.ui

    import android.content.Intent
    import android.net.Uri
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.lifecycleScope
    import androidx.navigation.fragment.findNavController
    import com.example.auth.R
    import com.example.auth.databinding.FragmentIntroductionBinding
    import com.example.auth.viewmodels.IntroductionViewModel
    import com.example.auth.viewmodels.IntroductionViewModel.Companion.HOMECHAT_ACTIVITY
    import com.example.auth.viewmodels.IntroductionViewModel.Companion.LOGIN_FRAGMENT
    import com.example.navigation.Routes
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
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Routes.CHAT_HOME)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }

            else -> Unit
        }
    }
            }

        binding.lnGetStarted.setOnClickListener {
            viewModel.startButtonClick()
                findNavController().navigate(com.example.navigation.R.id.action_introductionFragment_to_loginFragment)
            }
        }


    }