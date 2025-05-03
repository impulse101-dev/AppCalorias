package com.example.appcalorias.api.request

import com.example.appcalorias.config.ConfigLoader

data class PrompRequest(
    val model: String = ConfigLoader.getModel(),
    val prompt: String,
    val stream: Boolean = false,
    //val format : String = "json",
    val images : List<String> = emptyList(),    //no puedo enviar una sola imagen directamente...
    val raw : Boolean = ConfigLoader.getRaw() //si despues de un par de prompts funciona mal
)