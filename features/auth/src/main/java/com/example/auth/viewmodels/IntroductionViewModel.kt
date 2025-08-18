package com.example.auth.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code.common.utils.Constants.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) : ViewModel()  {

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object {
        const val HOMECHAT_ACTIVITY = 23
        val LOGIN_FRAGMENT = com.example.navigation.R.id.action_introductionFragment_to_loginFragment
    }
    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            viewModelScope.launch {
                _navigate.emit(HOMECHAT_ACTIVITY)
            }
        } else if (isButtonClicked) {
            viewModelScope.launch {
                _navigate.emit(LOGIN_FRAGMENT)
            }
        } else{
            Unit
        }
    }

    fun startButtonClick() {
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()
    }

}