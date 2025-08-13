package com.example.code.common.utils

import android.util.Patterns

fun validateEmail(email: String):RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Email cannot be emty")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")
    return RegisterValidation.Sucsses
}
fun validatePassword(password:String):RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password cannot be emty")
    if (password.length < 6)
        return RegisterValidation.Failed("Password should contains 6 char")
    return RegisterValidation.Sucsses

}