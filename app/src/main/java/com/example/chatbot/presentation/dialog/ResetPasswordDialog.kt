package com.example.chatbot.presentation.dialog

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatbot.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDiaLog(
    onSendClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_passwrd_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edEmail = view.findViewById<EditText>(R.id.edResetPassword)
    val btnSend = view.findViewById<Button>(R.id.buttonSendResetPassword)
    val btnCancel = view.findViewById<Button>(R.id.buttonCancelResetPassword)

    btnSend.setOnClickListener {
        val email = edEmail.text.toString().trim()
        if (email.isNotEmpty()) {
            onSendClick(email)
            dialog.dismiss()
        } else {
            Toast.makeText(requireContext(), "Email must not be empty", Toast.LENGTH_LONG).show()
        }
    }

    btnCancel.setOnClickListener {
        dialog.dismiss()
    }
}
