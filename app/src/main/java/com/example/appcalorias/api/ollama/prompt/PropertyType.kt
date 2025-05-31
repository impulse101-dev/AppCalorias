package com.example.appcalorias.api.ollama.prompt

/**
 * Data class que representa el tipo de dato que devuelva ollama.
 * @property type el tipo de dato, por defecto es "number".
 * @author Adrian Salazar Escoriza
 */
data class PropertyType(
    val type: String =  "number"
)