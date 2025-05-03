package com.example.appcalorias.api.response.get

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Model (
    @SerializedName("name") val name : String,
    @SerializedName("model") val model : String,
    @SerializedName("size") val size : BigInteger,
    @SerializedName("digest") val digest : String,
    @SerializedName("details") val details : Detail,
    @SerializedName("expires_at") val expiresAt: String,
    @SerializedName("size_vram") val sizeVram : BigInteger
)