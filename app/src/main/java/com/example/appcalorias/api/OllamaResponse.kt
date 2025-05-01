package com.example.appcalorias.api

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

//peticiones post de la api
data class PromptResponse (
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





//peticiones sencillas

data class ModelsResponse (
    @SerializedName("models") val result : Collection<Model>
)

data class Model (
    @SerializedName("name") val name : String,
    @SerializedName("model") val model : String,
    @SerializedName("size") val size : BigInteger,
    @SerializedName("digest") val digest : String,
    @SerializedName("details") val details : Detail,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("size_vram") val sizeVram : BigInteger
)

data class Detail (
    @SerializedName("parent_model") val parentModel: String,
    @SerializedName("format") val format : String,
    @SerializedName("family") val family: String,
    @SerializedName("families") val families: List<String>,
    @SerializedName("parameter_size") val parameterSize: String,
    @SerializedName("quantization_level") val quantizationLevel : String
)