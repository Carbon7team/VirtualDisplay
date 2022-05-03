package com.carbon7.virtualdisplay.ui.ups_selector

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.DialogCreateUpsBinding
import java.util.regex.Pattern

class ModifyUpsDialog(private val onSuccess:(id: Int, name: String, ip: String, port: Int)->Unit) :
    DialogFragment() {

    var zeroTo255 = ("(\\d{1,2}|(0|1)\\"
            + "d{2}|2[0-4]\\d|25[0-5])")

    var p: Pattern = Pattern.compile(
        (zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255)
    )

    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding = DialogCreateUpsBinding.inflate(LayoutInflater.from(context))

            builder.setTitle(R.string.add_new_ups)
                .setView(binding.root)
                .setPositiveButton(R.string.add,
                    DialogInterface.OnClickListener { dialog, id ->

                        // value format check
                        val ip = binding.upsIp.text.toString()
                        val port = binding.upsPort.text.toString().toInt()

                        if (p.matcher(ip).matches()) throw IllegalStateException("Activity cannot be null")
                        else if (port < 0 || port > 65535) throw IllegalStateException("Activity cannot be null")
                        else onSuccess(binding.upsName.text.toString(), ip, port)
                    })
                .setNegativeButton(R.string.cancel,
                    // default behaviour
                    DialogInterface.OnClickListener { dialog, id -> })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}