package com.example.appcalorias.config

import android.content.Context
import com.example.appcalorias.R
import com.example.appcalorias.exceptions.BadPropertyException

object ConfigLoader {

    private lateinit var ip: String
    private lateinit var port: String
    private lateinit var model: String
    private lateinit var modelRaw : String  //que no guarde el contexto
    /**
     * TODO
     * Acuerdate de al escribir / realizar exposicion, comentar lo de openRawResource
     */


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
        modelRaw = values["model_raw"] ?: throw BadPropertyException("No se ha encontrado el valor de raw (true / false)")
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

    fun getRaw() : Boolean {
        return modelRaw.toBoolean()
    }


}