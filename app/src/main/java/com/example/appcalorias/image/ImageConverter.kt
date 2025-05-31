package com.example.appcalorias.image

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import com.example.appcalorias.api.ollama.config.ConfigLoader
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale

/**
 * Objeto que se encarga de obtener una imagen y convertirla a base64.
 * De esta forma, podemos enviar la imagen a la API y obtener una respuesta.
 * @author Adrian Salazar Escoriza
 */
object ImageConverter {

    /**
     * Convierte una imagen a base64
     * @param drawable la imagen a convertir
     * @return La cadena de caracteres del drawable en base64 (Formateada para la api de ollama)
     */
    fun convertImageToBase64(drawable: Drawable): String {
        val bitmap = (drawable as BitmapDrawable).bitmap

// Escalar la imagen si excede la resolución máxima permitida
        val maxResolution = ConfigLoader.getImageMaxResolution()
        val scaledBitmap = if (bitmap.width > maxResolution || bitmap.height > maxResolution) {
            val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
            if (aspectRatio > 1) {
                // Imagen más ancha que alta
                bitmap.scale(maxResolution, (maxResolution / aspectRatio).toInt())
            } else {
                // Imagen más alta que ancha
                bitmap.scale((maxResolution * aspectRatio).toInt(), maxResolution)
            }
        } else {
            bitmap
        }

        val outputStream = ByteArrayOutputStream()
// Comprimir la imagen en formato PNG
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

// NO_WRAP es crucial - evita saltos de línea en el base64
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}