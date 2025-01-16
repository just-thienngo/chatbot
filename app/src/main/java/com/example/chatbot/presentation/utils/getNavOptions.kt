package com.example.chatbot.presentation.utils

import androidx.navigation.NavOptions
import com.example.chatbot.R

fun getNavOptions(): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.enter_right_to_left)
        .setExitAnim(R.anim.exit_left_to_right)
        .setPopEnterAnim(R.anim.enter_left_to_right)
        .setPopExitAnim(R.anim.exit_right_to_left)
        .build()
}