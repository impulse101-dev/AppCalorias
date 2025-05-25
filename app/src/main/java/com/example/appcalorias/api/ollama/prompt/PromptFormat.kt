package com.example.appcalorias.api.ollama.prompt

import com.example.appcalorias.api.ollama.config.ConfigLoader

data class PromptFormat(
    val type : String = ConfigLoader.getFormatType(),
    val properties : FormatProperties,
)
