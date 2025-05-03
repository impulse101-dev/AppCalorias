package com.example.appcalorias.api

import com.example.appcalorias.api.request.PrompRequest
import com.example.appcalorias.api.response.post.PromptResponse
import com.example.appcalorias.image.ImageConverter
import retrofit2.Response

/**
 * Clase que contiene los metodos para realizar las peticiones a la api
 * @author Adrian Salazar Escoriza
 */
class ApiUtilities {
    companion object {

        /**
         * Metodo que se encarga de obtener los modelos ejecutandose en la api (TESTING)
         */
        suspend fun getRunningModels() =
            RetrofitClient
                .retrofit
                .create(ApiService::class.java)
                .getResponse("api/ps")


        /**
         * Metodo que realiza un POST a la api con el prompt    (Metodo de testing, no se deberia de poder usar la aplicacion sin imagen)
         * @param userPrompt el prompt a enviar
         */
        suspend fun postPrompt(userPrompt: String): Response<PromptResponse> {
            val parameters = PrompRequest(
                prompt = userPrompt,
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
        suspend fun postPrompt(userPrompt: String, image : String) : Response<PromptResponse> {
            val parameters = PrompRequest(
                prompt = userPrompt,
                images = listOf(image)
            )

            return RetrofitClient
                .retrofit
                .create(ApiService::class.java)
                .generateRequest(parameters)

        }



    }

}