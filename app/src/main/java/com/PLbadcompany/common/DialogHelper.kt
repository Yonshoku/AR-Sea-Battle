package com.PLbadcompany.common

import android.app.AlertDialog
import android.content.Context

class DialogHelper {

    fun showDialog(context: Context, title: String?, message: String,
                          positiveButtonText: String, negativeButtonText: String,
                          positiveCallback: (() -> Unit)? = null, negativeCallback: (() -> Unit)? = null) {

        val builder = AlertDialog.Builder(context)
        
        if (title != null)
            builder.setTitle(title)

        builder.setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
            positiveCallback?.invoke()
            dialog.dismiss()
        }
            .setNegativeButton(negativeButtonText) { dialog, _ ->
            negativeCallback?.invoke()
            dialog.dismiss()
        }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

    }

    fun showOkDialog(context: Context, message: String,
                            callback: (() -> Unit)? = null) {

        val builder = AlertDialog.Builder(context)

        builder.setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                callback?.invoke()
                dialog.dismiss()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

}