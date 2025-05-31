package com.example.appcalorias.api.ollama

import com.example.appcalorias.api.ollama.config.ConfigLoader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton que crea una instancia de Retrofit para realizar peticiones a la API de Ollama.
 * En caso de que alguno de estos parametros no esten correctamente configurados, la funcion principal
 * de la aplicacion no funcionara correctamente.
 * @property BASE_URL La URL base de la API, configurada a partir de la IP y el puerto obtenidos del ConfigLoader.
 * @property loggingInterceptor Interceptor para registrar las peticiones y respuestas HTTP.
 * @property okHttpClient Cliente HTTP configurado con el interceptor de registro y tiempos de espera personalizados.
 * En este caso, los tiempos de espera son elevados dado a que las peticiones a la API pueden tardar bastante tiempo en completarse.
 * @property retrofit Instancia de Retrofit configurada con la URL base, el cliente HTTP y el convertidor de Gson.
 * @author Adrian Salazar Escoriza
 */
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