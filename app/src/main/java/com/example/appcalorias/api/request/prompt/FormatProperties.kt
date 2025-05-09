package com.example.appcalorias.api.request.prompt

data class FormatProperties(
    val calories: PropertyType,
    val carbohydrates: PropertyType,
    val proteins: PropertyType,
    val fats: PropertyType
)