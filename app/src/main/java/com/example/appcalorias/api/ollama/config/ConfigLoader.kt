package com.example.appcalorias.api.ollama.config

import android.content.Context
import com.example.appcalorias.R
import com.example.appcalorias.exceptions.BadPropertyException

object ConfigLoader {

    private lateinit var ip: String
    private lateinit var port: String

    /**
     * Modelo de IA que se va a utilizar.
     */
    private lateinit var model: String
    /**
     * La resolucion maxima que la IA procesara.
     * A mayor resolucion, mayor precision, pero mayor tiempo de espera.
     */
    private lateinit var imageMaxResolution : String

    /**
     * Peticiones de /api/generate
     */
    private lateinit var basePrompt : String

    /**
     * Propiedades obligatorias en la respuesta de la ia
     */
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

    fun getModel(): String {
        return model
    }

    fun getIp(): String {
        return ip
    }

    fun getPort(): String {
        return port
    }

    fun getImageMaxResolution(): Int {
        return imageMaxResolution.toInt()
    }

    fun getBasePrompt(): String {
        return basePrompt
    }

    fun getFormatType(): String {
        return formatType
    }

    fun getRequiredProperties(): List<String> {
        val trimmedPropeties = requiredProperties.map { it.trim() }
        //Log.d("trimmedPropeties", trimmedPropeties.toString())
        return trimmedPropeties
    }


}