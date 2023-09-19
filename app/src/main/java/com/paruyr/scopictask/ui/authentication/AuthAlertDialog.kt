package com.paruyr.scopictask.ui.authentication

import android.content.Context
import androidx.appcompat.app.AlertDialog

class AuthAlertDialog {
    companion object {
        fun showAlertDialog(context: Context, title: String, message: String) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ ->
                    // Positive button click handler
                    dialog.dismiss() // Dismiss the dialog when OK is clicked
                }
                .create()
                .show() // Show the dialog
        }
    }
}