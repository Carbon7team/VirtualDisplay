package com.carbon7.virtualdisplay.ui.ups_selector

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.carbon7.virtualdisplay.R

class DeleteUpsDialog(private val name: String, private val onSuccess: () -> Unit) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.confirm_delete)
                .setMessage(getString(R.string.confirm_reminder) + " \"" + name + "\"?")
                .setPositiveButton(R.string.delete) { _: DialogInterface, _: Int ->
                    Toast.makeText(context, getString(R.string.ups_deleted), Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
                .setNegativeButton(R.string.cancel, null)

            builder.create()
        } ?: throw IllegalStateException(getString(R.string.dialog_null))
    }
}