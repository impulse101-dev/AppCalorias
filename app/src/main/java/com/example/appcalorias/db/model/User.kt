package com.example.appcalorias.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var age: Int,       //la edad puede variar
    var weight : Int,   //el peso puede variar (es literalmente el objetivo del proyecto...)
    val gender : Boolean,    //todo no se si es lo mas correcto hacer esto booleano
    val height : Int,
)