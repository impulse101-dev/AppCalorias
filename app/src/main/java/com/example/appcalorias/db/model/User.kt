package com.example.appcalorias.db.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.res.DateUpdate
import com.example.appcalorias.db.model.res.Gender
import java.io.Serializable

@Entity
class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,      //el id se genera automaticamente, ha de ser 0 para que Room funcione correctamente
    val dateUpdate: String = DateUpdate.currentDate,
    val age: Int,       //la edad puede variar
    val weight: Int,   //el peso puede variar (es literalmente el objetivo del proyecto...)
    val gender: Gender,
    val height: Int,
)
    : Serializable
{

    /**
     * Basal Metabolic Rate (BMR) del usuario.
     * Este no esta contando las calorias que quema el usuario al realizar actividades.
     */
    var bmr: Int = calculateBMR(age, gender, height, weight)


    companion object {
        /**
         * Metodo que se encarga de devolver el utlimo usuario de la base de datos (la ultima modificacion de este).
         * @param context el contexto de la aplicacion.
         * @return el ultimo usuario de la base de datos o null si no hay usuarios.
         */
        suspend fun getLastUser(context: Context): User? {
            return DatabaseProvider.getDatabase(context).getUserDAO().getLastUser()
        }
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

}