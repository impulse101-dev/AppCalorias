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
            childColumns = ["idUser"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val date: String = DateUpdate.currentDate,
    val calories : Float,        //REAL
    val carbohydrates : Float,
    val proteins : Float,
    val fats : Float,
    val idUser : Int
)