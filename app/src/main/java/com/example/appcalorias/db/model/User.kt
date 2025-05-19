package com.example.appcalorias.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,      //el id se genera automaticamente, ha de ser 0 para que Room funcione correctamente
    var name: String,
    var age: Int,       //la edad puede variar
    var weight : Int,   //el peso puede variar (es literalmente el objetivo del proyecto...)
    var gender : Gender,
    var height : Int,
    var bmr : Int,
)