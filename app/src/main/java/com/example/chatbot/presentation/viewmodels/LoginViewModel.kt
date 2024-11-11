package com.example.chatbot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.domain.usecase.FacebookSignInUseCase
import com.example.chatbot.domain.usecase.GithubSignInUseCase
import com.example.chatbot.domain.usecase.GoogleSignInUseCase
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val facebookSignInUseCase: FacebookSignInUseCase,
    private val githubSignInUseCase: GithubSignInUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            val result = googleSignInUseCase(account)
            _loginResult.value = result
        }
    }

    fun firebaseAuthWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            val result = facebookSignInUseCase(token)
            _loginResult.value = result
        }
    }
    fun signInWithGithub(accessToken: String) {
        githubSignInUseCase(accessToken) { isSuccess ->
            _loginResult.value = isSuccess
        }
    }
}