package com.example.appcalorias.api.request.prompt

import com.example.appcalorias.config.ConfigLoader
import com.google.gson.annotations.SerializedName

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