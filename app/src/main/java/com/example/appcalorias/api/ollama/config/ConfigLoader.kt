package com.example.appcalorias.api.ollama.config

import android.content.Context
import com.example.appcalorias.R
import com.example.appcalorias.exceptions.BadPropertyException

/**
 * Singleton que se encarga de cargar las propiedades de la api desde el archivo properties.conf
 * @property ip la ip del servidor de la api
 * @property port el puerto del servidor de la api
 * @property model el modelo de IA que se va a utilizar
 * @property imageMaxResolution la resolucion maxima que se enviara a la IA
 * @property basePrompt el prompt base que se enviara a la IA
 * @property requiredProperties las propiedades requeridas en la respuesta de la IA
 * @property formatType el formato de la imagen que se enviara a la IA
 */
object ConfigLoader {

    private lateinit var ip: String
    private lateinit var port: String

    private lateinit var model: String
    private lateinit var imageMaxResolution : String
    private lateinit var basePrompt : String
    private lateinit var requiredProperties : List<String>
    private lateinit var formatType : String


    /**
     * Este metodo se debe de ejecutar en la actividad principal de la aplicacion.
     * Gracias a el, podremos cargar las propiedades de la aplicacion desde el archivo properties.conf
     * @param context contexto de la aplicacion
     */
    fun init (context: Context) {
        val values = context.resources.openRawResource(R.raw.properties)
            .bufferedReader()
            .readText()
            .trim()
            .split("\n")
            .associate {
                val (key, value) = it.split("=")
                key.trim() to value.trim()
            }

        model = values["model"] ?: throw BadPropertyException("No se ha encontrado el modelo")
        ip = values["ip"] ?: throw BadPropertyException("No se ha encontrado la ip")
        port = values["port"] ?: throw BadPropertyException("No se ha encontrado el puerto")
        imageMaxResolution = values["imageMaxResolution"] ?: throw BadPropertyException("No se ha encontrado la resolucion maxima de la imagen")
        basePrompt = values["basePrompt"] ?: throw BadPropertyException("No se ha encontrado el prompt base")
        requiredProperties = values["requiredProperties"]?.
            split(",") ?: throw BadPropertyException("No se han encontrado las propiedades requeridas")
        formatType = values["formatType"] ?: throw BadPropertyException("No se ha encontrado el formato de la imagen")
    }

    /**
     * Getter del modelo de IA.
     */
    fun getModel(): String {
        return model
    }

    /**
     * Getter de la IP.
     */
    fun getIp(): String {
        return ip
    }

    /**
     * Getter del puerto.
     */
    fun getPort(): String {
        return port
    }

    /**
     * Getter de la resolucion maxima de la imagen.
     */
    fun getImageMaxResolution(): Int {
        return imageMaxResolution.toInt()
    }

    /**
     * Getter del prompt base.
     */
    fun getBasePrompt(): String {
        return basePrompt
    }

    /**
     * Getter del tipo de formato de la imagen.
     */
    fun getFormatType(): String {
        return formatType
    }

    /**
     * Getter de las propiedades requeridas.
     */
    fun getRequiredProperties(): List<String> {
        val trimmedPropeties = requiredProperties.map { it.trim() }
        return trimmedPropeties
    }


}