package com.example.chatbot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.domain.usecase.GoogleSignInUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            val result = googleSignInUseCase(account)
            _loginResult.value = result
        }
    }
}