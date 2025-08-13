package com.example.code.common.utils

sealed class RegisterValidation {
    object Sucsses: RegisterValidation()
    data class Failed(val message:String): RegisterValidation()

}
data class RegisterFieldState(
    val email: RegisterValidation,
    val password: RegisterValidation
)