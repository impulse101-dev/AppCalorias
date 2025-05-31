package com.example.appcalorias.activities.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.appcalorias.databinding.DialogLoadingBinding

/**
 * DialogFragment que muestra un dialogo de carga.
 * @author Adrian Salazar Escoriza
 */
class LoadingDialog : DialogFragment() {

    private lateinit var b: DialogLoadingBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        b = DialogLoadingBinding.inflate(layoutInflater)

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(b.root)

        return dialog
    }
}