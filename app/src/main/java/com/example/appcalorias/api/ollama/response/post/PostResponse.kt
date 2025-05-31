package com.example.appcalorias.api.ollama.response.post

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

/**
 * Data class que representa la respuesta de la API al hacer una peticion de tipo POST.
 * @property model el modelo utilizado para la generacion de la respuesta.
 * @property createdAt la fecha y hora en la que se creo la respuesta.
 * @property response el texto de la respuesta generada por el modelo.
 * @property done indica si la generacion de la respuesta ha finalizado.
 * @property context una coleccion de enteros que representa el contexto utilizado en la generacion de la respuesta.
 * @property totalDuration la duracion total de la generacion de la respuesta en nanosegundos.
 * @property loadDuration la duracion de la carga del modelo en nanosegundos.
 * @property prc el numero de evaluaciones del prompt realizadas.
 * @property eCount el numero de evaluaciones realizadas.
 * @property eDuration la duracion de las evaluaciones realizadas en nanosegundos.
 * @author Adrian Salazar Escoriza
 */
data class PostResponse (
    @SerializedName("model") val model : String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("response") val response : String,
    @SerializedName("done") val done : Boolean,
    @SerializedName("context") val context : Collection<Int>,
    @SerializedName("total_duration") val totalDuration : BigInteger,
    @SerializedName("load_duration") val loadDuration : BigInteger,
    @SerializedName("prompt_eval_count") val prc : BigInteger,
    @SerializedName("eval_count") val eCount : BigInteger,
    @SerializedName("eval_duration") val eDuration : BigInteger,
)