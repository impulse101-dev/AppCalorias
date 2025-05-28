package com.example.appcalorias.activities

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.appcalorias.api.ollama.response.post.foodProperties.FoodProperties
import com.example.appcalorias.databinding.FoodPropertiesDialogBinding

class FoodPropertiesDialog(
    private val foodProperties: FoodProperties,
    private val onFoodPropertiesAccepted: (() -> Unit)? = null
)
    : DialogFragment() {

    private lateinit var b : FoodPropertiesDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        initProperties()
        return Dialog(requireContext()).apply {
            setContentView(b.root)
        }
    }

    private fun initProperties () {
        b = FoodPropertiesDialogBinding.inflate(layoutInflater)
        setText()
        setActionListeners()
    }

    private fun setText() {
        b.tvCaloriesValue.text = setPrettier(foodProperties.calories, "kcal")
        b.tvCarbohydratesValue.text = setPrettier(foodProperties.carbohydrates, "g")
        b.tvProteinsValue.text = setPrettier(foodProperties.protein, "g")
        b.tvFatsValue.text = setPrettier(foodProperties.fat, "g")
    }

    private fun setPrettier (string: String, value: String) : String {
        return string.plus("\t${value}")
    }

    private fun setActionListeners () {
        b.btCancelFoodProperties.setOnClickListener {
            dismiss()
        }

        b.btAceptarFoodProperties.setOnClickListener {
            onFoodPropertiesAccepted?.invoke()  //al pulsar aceptar, se ejecuta la funcion que se le haya pasado
            dismiss()
        }
    }

}