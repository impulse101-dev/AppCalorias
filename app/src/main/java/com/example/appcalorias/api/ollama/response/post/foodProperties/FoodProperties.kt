package com.example.appcalorias.api.ollama.response.post.foodProperties

/**
 * Data class la cual representa las propiedas de la comida.
 * @property calories las calorias de la comida
 * @property carbohydrates los carbohidratos de la comida
 * @property protein las proteinas de la comida
 * @property fat las grasas de la comida (dado a que el modelo llama3.2-vision no era capaz de detectar las grasas, realmente son los azucares)
 * @author Adrian Salazar Escoriza
 */
data class FoodProperties (
    val calories: String,
    val carbohydrates: String,
    val protein: String,
    val fat: String,    //realmente son los azucares...
)