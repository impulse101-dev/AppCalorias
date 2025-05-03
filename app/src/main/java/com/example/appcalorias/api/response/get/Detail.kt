package com.example.appcalorias.api.response.get

import com.google.gson.annotations.SerializedName

data class Detail (
    @SerializedName("parent_model") val parentModel: String,
    @SerializedName("format") val format : String,
    @SerializedName("family") val family: String,
    @SerializedName("families") val families: List<String>,
    @SerializedName("parameter_size") val parameterSize: String,
    @SerializedName("quantization_level") val quantizationLevel : String
)
