package com.example.appcalorias.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.appcalorias.db.model.res.DateUpdate

/**
 * Clase que representa un registro de calorías consumidas por un usuario.
 * Cada registro incluye la fecha, las calorías, carbohidratos, proteínas, grasas y el ID del usuario asociado.
 * @property id el identificador único del registro, autogenerado.
 * @property date la fecha del registro, por defecto es la fecha actual.
 * @property calories las calorías consumidas en el registro.
 * @property carbohydrates los carbohidratos consumidos en el registro.
 * @property proteins las proteínas consumidas en el registro.
 * @property fats las grasas consumidas en el registro.
 * @property idUser el identificador del usuario al que pertenece el registro.(Clave foranea)
 * @author Adrian Salazar Escoriza
 */
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