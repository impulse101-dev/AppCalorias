package com.example.appcalorias.db.model.res

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

/**
 * Objeto que se encarga de dar devolver la hora y fecha de la ultima actualizacion del usuario.
 * @author Adrian Salazar Escoriza
 */
object DateUpdate {
    /**
     * Devuelve la fecha y hora actual formateada.
     * @return la fecha y hora actual en formato "yyyy-MM-dd HH:mm:ss".
     */
    val currentDate : String get() = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    ).toString()

    private const val FORMATPATTERN = "yyyy-MM-dd"

    /**
     * Calcula la edad actual del usuario a partir de su fecha de nacimiento.
     * @param birthDateTime la fecha de nacimiento del usuario en formato "yyyy-MM-dd".
     * @return la edad actual desde la fecha pasada por parametro.
     */
    fun getCurrentAge (birthDateTime: String) : Int {
        val formatter = DateTimeFormatter.ofPattern(FORMATPATTERN)
        val birthDate = LocalDate.parse(birthDateTime, formatter)

        return Period.between(birthDate, LocalDate.now()).years
    }


    /**
     * Calcula una fecha de nacimiento estimada a partir de una edad
     * @param age Edad
     * @return Fecha con el formato predeterminado de la aplicacion
     */
    fun getBirthDay (age : Int) : String {
        val date = LocalDate.now().minusYears(age.toLong())
        return date.format(DateTimeFormatter.ofPattern(FORMATPATTERN))
    }
}