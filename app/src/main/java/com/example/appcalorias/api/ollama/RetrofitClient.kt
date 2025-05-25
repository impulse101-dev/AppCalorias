package com.example.appcalorias.api.ollama

import com.example.appcalorias.api.ollama.config.ConfigLoader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val BASE_URL = "http://" + ConfigLoader.getIp() + ":" + ConfigLoader.getPort() + "/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY   //esto muestra el cuerpo de la peticion
    }

    private val okHttpClient = OkHttpClient.Builder()
        //.connectTimeout(90, TimeUnit.SECONDS)    // Aumentado a 90 segundos
        .readTimeout(120, TimeUnit.SECONDS)      // Aumentado a 2 minutos
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}