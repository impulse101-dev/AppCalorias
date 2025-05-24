package com.example.appcalorias.api.ollama.prompt

/**
 * Data class la cual representa el formato de las propiedades.
 * En la version de ollama ()
 * @author Adrian Salazar Escoriza
 */
data class FormatProperties(
    val calories: PropertyType,
    val carbohydrates: PropertyType,
    val proteins: PropertyType,
    //val grasas: PropertyType,
    val sugars : PropertyType
)