package com.example.appcalorias.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,      //el id se genera automaticamente, ha de ser 0 para que Room funcione correctamente
    //var name: String,
    var age: Int,       //la edad puede variar
    var weight : Int,   //el peso puede variar (es literalmente el objetivo del proyecto...)
    var gender : Gender,
    var height : Int,
    var bmr : Int,
    //var image : String = "https://api.dicebear.com/9.x/lorelei-neutral/png?seed=${name.replace(" ", "")}"
)

//{
    //la imagen no debe de ser puesta en el constructor
//    private var _image : String = "https://api.dicebear.com/9.x/lorelei-neutral/svg?seed=${name.replace(" ", "")}"

//    val image : String get() = _image      //cada vez que se llame a esta propiedad, se cargara la imagen que tiene
//}