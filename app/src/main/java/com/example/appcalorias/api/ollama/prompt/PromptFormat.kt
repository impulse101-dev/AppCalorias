package com.example.appcalorias.api.ollama.prompt

import com.example.appcalorias.api.ollama.config.ConfigLoader

/**
 * Data class que representa el formato del prompt.
 * @property type el tipo de formato, por defecto es el tipo configurado en ConfigLoader.
 * @property properties las propiedades del formato, que son instancias de FormatProperties.
 * @author Adrian Salazar Escoriza
 */
data class PromptFormat(
    val type : String = ConfigLoader.getFormatType(),
    val properties : FormatProperties,
)
