package com.example.appcalorias.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.appcalorias.db.model.res.DateUpdate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["idUser"]
        )
    ]
)
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val date: String = DateUpdate.currentDate,
    val calories : Float,        //REAL
    val carbohydrates : Int,
    val proteins : Int,
    val fats : Int,
    val idUser : Int
)