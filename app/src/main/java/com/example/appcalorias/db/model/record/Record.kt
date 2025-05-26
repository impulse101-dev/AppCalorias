package com.example.appcalorias.db.model.record

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.appcalorias.db.model.user.User

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
    val calories : Float,        //REAL
    val carbohydrates : Int,
    val proteins : Int,
    val fats : Int,
    val idUser : Int
)