package com.example.appcalorias.api.ollama.response.post

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

/**
 * Data class que representa la respuesta de la API al hacer una peticion de tipo POST.
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