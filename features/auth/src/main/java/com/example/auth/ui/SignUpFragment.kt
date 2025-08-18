package com.example.auth.ui


import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.auth.R
import com.example.auth.databinding.FragmentSignupBinding
import com.example.auth.viewmodels.SignUpViewModel
import com.example.code.common.utils.RegisterValidation
import com.example.code.common.utils.Resource
import com.example.commom_entity.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel by viewModels<SignUpViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
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

        binding.btnSignup.setOnClickListener {
            val user = User(
                binding.edFullName.text.toString().trim(),
                binding.edEmailRegister.text.toString().trim()
            )
            val password = binding.edPasswordRegister.text.toString()
            viewModel.createAccountWithEmailAndPassword(user, password)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.register.collect {
                        when (it) {
                            is Resource.Loading -> binding.btnSignup.startAnimation()
                            is Resource.Success -> {
                                binding.btnSignup.revertAnimation()
                                Toast.makeText(requireContext(),"Account created successfully!", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(com.example.navigation.R.id.action_signUpFragment_to_loginFragment)
                            }
                            is Resource.Error -> {
                                binding.btnSignup.revertAnimation()
                                Toast.makeText(requireContext(), it.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                            }
                            else -> Unit
                        }
                    }
                }
                launch {
                    viewModel.validation.collect { validation ->
                        binding.edEmailRegister.error =
                            (validation.email as? RegisterValidation.Failed)?.message

                        binding.edPasswordRegister.error =
                            (validation.password as? RegisterValidation.Failed)?.message
                    }
                }
            }
        }
        var isPasswordVisible = false
        binding.ivTogglePassword.setImageResource(com.example.code.common.R.drawable.ic_eye_closed)
        binding.ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {

                binding.edPasswordRegister.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.ivTogglePassword.setImageResource(com.example.code.common.R.drawable.ic_eye_open) // icon mắt mở
            } else {

                binding.edPasswordRegister.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ivTogglePassword.setImageResource(com.example.code.common.R.drawable.ic_eye_closed) // icon mắt đóng
            }

            binding.edPasswordRegister.setSelection(binding.edPasswordRegister.text?.length ?: 0)
        }

    }
}