package com.carbon7.virtualdisplay.ui.ups_selector

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.DialogCreateUpsBinding
import java.util.regex.Pattern

class CreateNewUpsDialog(private val onSuccess: (name: String, ip: String, port: Int) -> Unit) :
    DialogFragment() {

    private var _binding: DialogCreateUpsBinding? = null
    private val binding
        get() = _binding!!

    private var zeroTo255 = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])"

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
                binding.upsName.error = getString(R.string.empty_ups_name)
            }

            val ip = binding.upsIp.text.toString()
            if (!p.matcher(ip).matches()) {
                correctInput = false
                binding.upsIp.error = getString(R.string.ip_not_correct)
            }

            try {
               val port = binding.upsPort.text.toString().toInt()
                if (port > 65535) {
                    correctInput = false
                    binding.upsPort.error = getString(R.string.port_error)
                }
                if (correctInput) {
                    dismiss()
                    Toast.makeText(context, getString(R.string.ups_added), Toast.LENGTH_SHORT).show()
                    onSuccess(name, ip, port)
                }

            } catch (e: NumberFormatException) {
                binding.upsPort.error = getString(R.string.port_empty)
            }
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
                    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                }

            builder.create()
        } ?: throw IllegalStateException(getString(R.string.dialog_null))
    }
}