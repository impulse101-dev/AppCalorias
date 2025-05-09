package com.example.appcalorias

import android.app.Dialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.example.appcalorias.databinding.FoodPropertiesDialogBinding

class FoodPropertiesDialog : DialogFragment() {
    val binding = FoodPropertiesDialogBinding.inflate(layoutInflater)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
}