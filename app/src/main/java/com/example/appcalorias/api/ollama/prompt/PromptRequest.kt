package com.example.appcalorias.api.ollama.prompt

import com.example.appcalorias.api.ollama.config.ConfigLoader
import com.google.gson.annotations.SerializedName

/**
 * Data class que representa una solicitud de prompt.
 * @property model el modelo a utilizar, por defecto es el configurado en ConfigLoader.
 * @property prompt el prompt a enviar, por defecto es el base prompt configurado en ConfigLoader.
 * @property stream indica si se debe utilizar streaming, por defecto es false.
 * @property format el formato del prompt, por defecto es una instancia de PromptFormat con las propiedades configuradas.
 * @property images las imágenes a enviar, por defecto es una colección vacía.
 * @property required las propiedades requeridas en la respuesta, por defecto es la colección configurada en ConfigLoader.
 * @author Adrian Salazar Escoriza
 */
data class PromptRequest(
    @SerializedName("model") val model: String = ConfigLoader.getModel(),
    @SerializedName("prompt") val prompt: String = ConfigLoader.getBasePrompt(),
    @SerializedName("stream") val stream: Boolean = false,
    @SerializedName("format") val format : PromptFormat = PromptFormat(
        properties = FormatProperties(
            calories = PropertyType(),
            carbohydrates = PropertyType(),
            proteins = PropertyType(),
            //grasas = PropertyType(),
            sugars = PropertyType()
        )
    ),
    @SerializedName("images") val images : Collection<String> = emptyList(),
    @SerializedName("required") val required : Collection<String> = ConfigLoader.getRequiredProperties()
)