package com.example.appcalorias.api.ollama.response.post.foodProperties

/**
 * Data class la cual representa las propiedas de la comida.
 * @author Adrian Salazar Escoriza
 */
data class FoodProperties (
    val calories: String,
    val carbohydrates: String,
    val protein: String,
    val fat: String,    //realmente son los azucares...
)