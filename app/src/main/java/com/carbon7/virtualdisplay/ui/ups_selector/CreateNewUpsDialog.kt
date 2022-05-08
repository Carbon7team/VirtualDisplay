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

class CreateNewUpsDialog(private val onSuccess: (name: String, ip: String, port: Int) -> Unit) :
    DialogFragment() {

    private var _binding: DialogCreateUpsBinding? = null
    private val binding
        get() = _binding!!

    private var zeroTo255 = ("(\\d{1,2}|(0|1)\\"
            + "d{2}|2[0-4]\\d|25[0-5])")

    private var p: Pattern = Pattern.compile(
        (zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255)
    )

    override fun onResume() {
        super.onResume()
        (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            // value format check
            var correctInput = true

            val name = binding.upsName.text.toString()
            if (name == "") {
                correctInput = false
                binding.upsName.error = "UPS NAME CAN NOT BE EMPTY!"
            }

            val ip = binding.upsIp.text.toString()
            if (!p.matcher(ip).matches()) {
                correctInput = false
                binding.upsIp.error = "IP ADDRESS WRONG FORMAT!"
            }

            try {
               val port = binding.upsPort.text.toString().toInt()
                if (port < 0 || port > 65535) {
                    correctInput = false
                    binding.upsPort.error = "UPS NAME CAN NOT BE EMPTY!"
                }
                if (correctInput) {
                    dismiss()
                    onSuccess(name, ip, port)
                }

            } catch (e: NumberFormatException) {
                binding.upsPort.error = "UPS NAME CAN NOT BE EMPTY!"
            }

            binding.upsName.error = null
            binding.upsIp.error = null
            binding.upsPort.error = null

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            _binding = DialogCreateUpsBinding.inflate(LayoutInflater.from(context))

            builder.setTitle(R.string.add_new_ups)
                .setView(binding.root)
                .setPositiveButton(R.string.add, null)
                .setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
                    binding.upsName.error = null
                    binding.upsIp.error = null
                    binding.upsPort.error = null
                }

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}