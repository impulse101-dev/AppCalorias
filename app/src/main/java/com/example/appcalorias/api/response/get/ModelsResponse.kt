package com.example.appcalorias.api.response.get

import com.google.gson.annotations.SerializedName

data class ModelsResponse (
    @SerializedName("models") val result : Collection<Model>
)