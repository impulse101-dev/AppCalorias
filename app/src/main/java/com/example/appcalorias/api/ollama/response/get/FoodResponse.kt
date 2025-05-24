package com.example.appcalorias.api.ollama.response.get

data class FoodResponse(
    val isFood : Boolean,
    val calories : Float,
    val carbohydrates : Float,
    val protein : Float,
    val fat : Float,
)


//todo HACER ALGO CON ESTA CLASE