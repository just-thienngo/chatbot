package com.example.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code.common.utils.RegisterFieldState
import com.example.code.common.utils.RegisterValidation
import com.example.code.common.utils.Resource
import com.example.code.common.utils.validateEmail
import com.example.code.common.utils.validatePassword
import com.example.commom_entity.User

import com.example.usecase.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {


    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())

    val register: Flow<Resource<User>> = _register
    private  val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        if (checkValidation(user, password)) {
            viewModelScope.launch {
                _register.emit(Resource.Loading())
                when(val result = authUseCase.createAccountWithEmailAndPassword(user, password)){
                    is Resource.Success -> {
                        _register.value = Resource.Success(user)
                    }
                    is Resource.Error -> {
                        _register.value = Resource.Error(result.message.toString())
                    }
                    else -> Unit
                }
            }
        } else {
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )
            viewModelScope.launch {
                _validation.send(registerFieldState)
            }
        }
    }
    private fun checkValidation(user: User, password: String):Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Sucsses &&
                passwordValidation is RegisterValidation.Sucsses
        return shouldRegister
    }
}