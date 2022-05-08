package com.carbon7.virtualdisplay.ui.ups_selector

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.carbon7.virtualdisplay.R
import com.carbon7.virtualdisplay.databinding.DialogCreateUpsBinding
import java.io.IOException
import java.util.regex.Pattern

class ModifyUpsDialog(private var name: String, private val ip: String, private val port: Int, private val onSuccess: (name: String, ip: String, port: Int) -> Unit) :
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
            try {
                val name = binding.upsName.text.toString()
                val ip = binding.upsIp.text.toString()
                val port = binding.upsPort.text.toString().toInt()

                if (name == "") throw IOException("UPS NAME CAN NOT BE EMTY!")
                if (!p.matcher(ip).matches()) throw IOException("IP ADDRESS FORMAT IS NOT CORRECT!")
                else if (port < 0 || port > 65535) throw IOException("PORT NUMBER VALUE IS NOT CORRECT!")
                else {
                    Toast.makeText(context, "UPS ADDED", Toast.LENGTH_SHORT).show()
                    onSuccess(name, ip, port)
                }

            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "WRONG FORMAT OF VALUES!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding = DialogCreateUpsBinding.inflate(LayoutInflater.from(context))

            binding.upsName.setText(name)
            binding.upsIp.setText(ip)
            binding.upsPort.setText(port.toString())

            builder.setTitle(R.string.modify_this_ups)
                .setView(binding.root)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}