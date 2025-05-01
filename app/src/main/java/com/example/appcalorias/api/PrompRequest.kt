package com.example.appcalorias.api

import com.example.appcalorias.config.ConfigLoader

data class PrompRequest(
    val model: String = ConfigLoader.getModel(),
    val prompt: String,
    val stream: Boolean = false,
    //val format : String = "json",
    val image : String? = null,
    val raw : Boolean = ConfigLoader.getRaw() //si despues de un par de prompts funciona mal
)
