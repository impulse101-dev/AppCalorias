package com.example.appcalorias.api.ollama

import com.example.appcalorias.api.ollama.prompt.PromptRequest
import com.example.appcalorias.api.ollama.response.post.PostResponse
import com.example.appcalorias.api.ollama.config.ConfigLoader
import retrofit2.Response

/**
 * Clase que contiene los metodos para realizar las peticiones a la api
 * @author Adrian Salazar Escoriza
 */
class ApiUtilities {
    companion object {

        /**
         * Metodo que realiza un POST a la api con el prompt (Metodo principal de la aplicacion)
         * @param image La imagen a enviar.
         */
        suspend fun postPrompt(image: String): Response<PostResponse> {
            val parameters = PromptRequest(
                images = listOf(image),
            )

            //println(parameters.asArguments())

            return RetrofitClient
                .retrofit
                .create(ApiService::class.java)
                .generateRequest(parameters)
        }

        /**
         * Metodo que realiza un POST a la api con el prompt y la imagen
         * @param userPrompt el prompt a enviar
         * @param image la imagen a enviar - En base64
         */
        suspend fun postPrompt(userPrompt: String, image : String) : Response<PostResponse> {
            val parameters = PromptRequest(
                prompt = ConfigLoader.getBasePrompt().plus(userPrompt),
                images = listOf(image)
            )

            return RetrofitClient
                .retrofit
                .create(ApiService::class.java)
                .generateRequest(parameters)

        }



    }

}