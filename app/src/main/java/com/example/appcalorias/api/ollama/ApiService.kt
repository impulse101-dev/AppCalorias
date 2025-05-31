package com.example.appcalorias.api.ollama

import com.example.appcalorias.api.ollama.prompt.PromptRequest
import com.example.appcalorias.api.ollama.response.post.PostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz que define los servicios de la API.
 * En este caso, se define un servicio para realizar peticiones POST a la API de Ollama.
 * @author Adrian Salazar Escoriza
 */
interface ApiService {
    /**
     * Realiza una peticion POST a la api
     * @param body de la peticion
     */
    @POST("api/generate")
    suspend fun generateRequest(@Body body: PromptRequest) : Response<PostResponse>

}