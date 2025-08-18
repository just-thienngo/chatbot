package com.example.auth.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code.common.utils.Resource
import com.example.usecase.auth.AuthUseCase
import com.example.usecase.auth.SocialAuthUseCase
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialAuthUseCase: SocialAuthUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val loginResult: StateFlow<Resource<FirebaseUser>> = _loginResult.asStateFlow()



    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()


    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            val result = socialAuthUseCase(account)
            _loginResult.value = result
        }
    }

    fun firebaseAuthWithFacebook(token: AccessToken) {
        viewModelScope.launch {
            val result = socialAuthUseCase(token)
            _loginResult.value = result
        }
    }
    
    fun signInWithGithub(accessToken: String) {
        viewModelScope.launch {
            val result = socialAuthUseCase(accessToken)
            _loginResult.value = result
        }
    }
    fun exchangeGithubCodeForToken(code: String){
        viewModelScope.launch {
            val result = socialAuthUseCase(code)
            _loginResult.value = result
        }
    }
    fun signInWithEmailAndPassword(email: String, password: String){
        viewModelScope.launch {
            _loginResult.value = Resource.Loading()
            val result = authUseCase(email, password)
            _loginResult.value = result
        }
    }
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
            val result = authUseCase.resetPassword(email)
            _resetPassword.emit(result) // Emit kết quả (Success/Error)
        }

    }
}