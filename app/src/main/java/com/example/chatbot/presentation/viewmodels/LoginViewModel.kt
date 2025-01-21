package com.example.chatbot.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.domain.usecase.auth.EmailPasswordSignInUseCase
import com.example.chatbot.domain.usecase.auth.FacebookSignInUseCase
import com.example.chatbot.domain.usecase.auth.GithubSignInUseCase
import com.example.chatbot.domain.usecase.auth.GoogleSignInUseCase
import com.example.chatbot.domain.usecase.auth.ResetPasswordUseCase
import com.example.chatbot.presentation.utils.Resource
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val facebookSignInUseCase: FacebookSignInUseCase,
    private val githubSignInUseCase: GithubSignInUseCase,
    private val emailPasswordSignInUseCase: EmailPasswordSignInUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<FirebaseUser>>(Resource.Unspecified())
    val loginResult: LiveData<Resource<FirebaseUser>> get() = _loginResult


    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()


    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            val result = googleSignInUseCase(account)
            _loginResult.value = if(result) Resource.Success(null) else Resource.Error("Google Sign In Failed")
        }
    }

    fun firebaseAuthWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            val result = facebookSignInUseCase(token)
            _loginResult.value = if(result) Resource.Success(null) else Resource.Error("Facebook Sign In Failed")
        }
    }
    fun signInWithGithub(accessToken: String) {
        viewModelScope.launch {
            val result = githubSignInUseCase(accessToken)
            _loginResult.value = result
        }
    }
    fun exchangeGithubCodeForToken(code: String){
        viewModelScope.launch {
            val result = githubSignInUseCase(code)
            _loginResult.value = result
        }
    }
    fun signInWithEmailAndPassword(email: String, password: String){
        viewModelScope.launch {
            val result = emailPasswordSignInUseCase(email,password)
            _loginResult.value = result
        }
    }
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
        viewModelScope.launch {
            val result = resetPasswordUseCase(email)
            _resetPassword.emit(result)
        }

    }
}