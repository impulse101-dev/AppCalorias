package com.example.appcalorias.db.model.res

import java.time.LocalDateTime
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
    val currentDate = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    ).toString()
}