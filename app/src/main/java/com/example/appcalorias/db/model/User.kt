package com.example.appcalorias.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appcalorias.db.model.res.DateUpdate
import com.example.appcalorias.db.model.res.Gender
import java.io.Serializable

/**
 * Clase que representa un usuario en la base de datos.
 * Cada usuario tiene un identificador único, fecha de actualización, edad, peso, género y altura.
 * Además, se calcula el BMR (Tasa Metabólica Basal) del usuario en función de estos datos.
 * @param id el identificador único del usuario, autogenerado por la base de datos. (No se debe modificar manualmente)
 * @param dateUpdate la fecha de la última actualización del usuario, por defecto es la fecha actual. (Recomendable no modificar)
 * @param age la edad del usuario, debe ser un valor positivo.
 * @param weight el peso del usuario, debe ser un valor positivo.
 * @param gender genero del usuario
 * @param height la altura del usuario, debe ser un valor positivo.
 * @property bmr la Tasa Metabólica Basal del usuario, calculada a partir de la edad, género, altura y peso.
 * @property PREFS_USER_ID clave para almacenar el ID del usuario en las preferencias compartidas.
 * (Utilizado para pasar el usuario principal por un intent)
 * @author Adrian Salazar Escoriza
 */
@Entity
class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,      //el id se genera automaticamente, ha de ser 0 para que Room funcione correctamente
    val dateUpdate: String = DateUpdate.currentDate,
    val age: Int,       //la edad puede variar
    val weight: Int,   //el peso puede variar (es literalmente el objetivo del proyecto...)
    val gender: Gender,
    val height: Int,
) : Serializable {

    var bmr: Int = calculateBMR(age, gender, height, weight)


    companion object {
        const val PREFS_USER_ID = "PREFS_USER"
    }

    /**
     * Este metodo se encarga de calcular el BMR (Basal Metabolic Rate) del usuario.
     * @param age la edad del usuario.
     * @param gender el genero del usuario.
     * @param height la altura del usuario.
     * @param weight el peso del usuario.
     * @return el BMR del usuario formateado a Int.
     */
    private fun calculateBMR(age: Int, gender: Gender, height: Int, weight: Int): Int {
        return if (gender == Gender.MALE)
            ((10 * weight) + (6.25 * height) - (5 * age) + 5).toInt()
        else
            ((10 * weight) + (6.25 * height) - (5 * age) - 161).toInt()
    }

    override fun toString(): String {
        return "User(id=$id, dateUpdate='$dateUpdate', age=$age, weight=$weight, gender=$gender, height=$height, bmr=$bmr)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is User) return false

        return id == other.id &&
                dateUpdate == other.dateUpdate &&
                age == other.age &&
                weight == other.weight &&
                gender == other.gender &&
                height == other.height &&
                bmr == other.bmr

    }

}