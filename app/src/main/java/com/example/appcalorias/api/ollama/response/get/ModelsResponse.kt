package com.example.appcalorias.api.ollama.response.get

import com.google.gson.annotations.SerializedName

data class ModelsResponse (
    @SerializedName("models") val result : Collection<Model>
)