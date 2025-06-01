package com.example.appcalorias.activities.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.appcalorias.api.ollama.response.post.foodProperties.FoodProperties
import com.example.appcalorias.databinding.FoodPropertiesDialogBinding

/**
 * DialogFragment que muestra las propiedades de un alimento.
 * @param foodProperties Objeto que contiene las propiedades del alimento.
 * @param onFoodPropertiesAccepted Función que se ejecuta al pulsar el botón de aceptar.
 * @author Adrian Salazar Escoriza
 */
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

    /**
     * Inicializa las propiedades del dialogo.
     */
    private fun initProperties () {
        b = FoodPropertiesDialogBinding.inflate(layoutInflater)
        setText()
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        setActionListeners()
    }

    /**
     * Establece el texto de los TextViews del dialogo con las propiedades del alimento.
     */
    private fun setText() {
        b.tvCaloriesValue.text = setPrettier(foodProperties.calories, "kcal")
        b.tvCarbohydratesValue.text = setPrettier(foodProperties.carbohydrates, "g")
        b.tvProteinsValue.text = setPrettier(foodProperties.protein, "g")
        b.tvFatsValue.text = setPrettier(foodProperties.fat, "g")
    }

    /**
     * Añade un tabulador y el valor al string.
     * @param string El string al que se le va a añadir el valor.
     * @return El string con el tabulador y el valor añadido.
     */
    private fun setPrettier (string: String, value: String) : String {
        return string.plus("\t${value}")
    }

    /**
     * Establece los listeners de los botones del dialogo.
     */
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