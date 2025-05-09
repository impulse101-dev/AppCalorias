package com.example.appcalorias.api.request.prompt

import com.example.appcalorias.config.ConfigLoader

data class PromptFormat(
    val type : String = ConfigLoader.getFormatType(),
    val properties : FormatProperties,
)
