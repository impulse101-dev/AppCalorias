package com.example.appcalorias.api

import com.example.appcalorias.config.ConfigLoader
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL = "http://" + ConfigLoader.getIp() + ":" + ConfigLoader.getPort() + "/"

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}