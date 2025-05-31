package com.example.appcalorias.api.ollama.prompt

/**
 * Data class la cual representa el formato de las propiedades.
 * @property calories las calorias de la comida
 * @property carbohydrates los carbohidratos de la comida
 * @property proteins las proteinas de la comida
 * @property sugars los azucares de la comida (dado a que el modelo llama3.2-vision no era capaz de detectar las grasas)
 * @author Adrian Salazar Escoriza
 */
data class FormatProperties(
    val calories: PropertyType,
    val carbohydrates: PropertyType,
    val proteins: PropertyType,
    //val grasas: PropertyType,
    val sugars : PropertyType
)