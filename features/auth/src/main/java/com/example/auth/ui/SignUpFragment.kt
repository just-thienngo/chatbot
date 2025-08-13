package com.example.auth.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.auth.R
import com.example.auth.databinding.FragmentSignupBinding
import com.example.auth.viewmodels.SignUpViewModel
import com.example.code.common.utils.RegisterValidation
import com.example.code.common.utils.Resource
import com.example.commom_entity.User
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(com.example.navigation.R.id.action_signUpFragment_to_loginFragment)
        }
        binding.tvHaveAccount.setOnClickListener {
            findNavController().navigate(com.example.navigation.R.id.action_signUpFragment_to_loginFragment)
        }

        binding.apply {
            btnSignup.setOnClickListener {
                val user = User(
                    edFullName.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )

                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSignup.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnSignup.revertAnimation()
                        findNavController().navigate(com.example.navigation.R.id.action_signUpFragment_to_loginFragment)
                    }
                    is Resource.Error -> {
                        binding.btnSignup.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                val emailValidation = validation.email
                if (emailValidation is RegisterValidation.Failed) {
                    binding.edEmailRegister.apply {
                        requestFocus()
                        error = emailValidation.message
                    }
                }

                val passwordValidation = validation.password
                if (passwordValidation is RegisterValidation.Failed) {
                    binding.edPasswordRegister.apply {
                        requestFocus()
                        error = passwordValidation.message
                    }
                }
            }

            }
    }
}