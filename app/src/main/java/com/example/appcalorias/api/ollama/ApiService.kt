package com.example.appcalorias.api.ollama

import com.example.appcalorias.api.ollama.prompt.PromptRequest
import com.example.appcalorias.api.ollama.response.get.ModelsResponse
import com.example.appcalorias.api.ollama.response.post.PostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    /**
     * Realiza una peticion GET a la api
     * @param url a la que enviar la peticion
     */
    @GET
    suspend fun getResponse(@Url url: String) : Response<ModelsResponse>

    /**
     * Realiza una peticion POST a la api
     * @param body de la peticion
     */
    @POST("api/generate")
    suspend fun generateRequest(@Body body: PromptRequest) : Response<PostResponse>

}