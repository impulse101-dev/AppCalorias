package com.example.appcalorias

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.appcalorias.api.response.post.foodProperties.FoodProperties
import com.example.appcalorias.databinding.FoodPropertiesDialogBinding

class FoodPropertiesDialog(private val foodProperties : FoodProperties)
    : DialogFragment() {
    private lateinit var b : FoodPropertiesDialogBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        b = FoodPropertiesDialogBinding.inflate(layoutInflater)
        setText()
        return Dialog(requireContext()).apply {
            setContentView(b.root)
        }
    }


    private fun setText() {
        b.calories.text = b.calories.text.toString().plus("\t${foodProperties.calories}kcal")
        b.carbohydrates.text = b.carbohydrates.text.toString().plus("\t${foodProperties.carbohydrates}")
        b.proteins.text = b.proteins.text.toString().plus("\t${foodProperties.protein}")
        b.fats.text = b.fats.text.toString().plus("\t${foodProperties.fat}")
    }

}