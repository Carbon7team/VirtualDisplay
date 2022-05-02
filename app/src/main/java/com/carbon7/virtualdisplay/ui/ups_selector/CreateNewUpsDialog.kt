package com.carbon7.virtualdisplay.ui.ups_selector

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.DialogCreateUpsBinding

class CreateNewUpsDialog(private val onSuccess:(name: String, ip: String, port: Int)->Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val binding = DialogCreateUpsBinding.inflate(LayoutInflater.from(context))

            builder.setTitle(R.string.add_new_ups)
                .setView(binding.root)
                .setPositiveButton(R.string.add,
                    DialogInterface.OnClickListener { dialog, id ->
                        // START THE GAME!
                        onSuccess(binding.upsName.text.toString(),
                            binding.upsIp.text.toString(),
                            binding.upsPort.text.toString().toInt())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog

                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}