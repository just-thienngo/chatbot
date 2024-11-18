package com.example.chatbot.presentation.utils

sealed class RegisterValidation {
    object Sucsses: RegisterValidation()
    data class Failed(val message:String): RegisterValidation()

}
data class RegisterFieldState(
    val email: RegisterValidation,
    val password: RegisterValidation
)