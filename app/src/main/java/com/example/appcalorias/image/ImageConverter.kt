package com.example.appcalorias.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import com.example.appcalorias.exceptions.FailedCompressionException
import java.io.ByteArrayOutputStream
import kotlin.compareTo
import kotlin.text.toInt
import kotlin.times
import androidx.core.graphics.scale

/**
 * Objeto que se encarga de obtener una imagen y convertirla a base64.
 * De esta forma, podemos enviar la imagen a la API y obtener una respuesta.
 * @author Adrian Salazar Escoriza
 */
object ImageConverter {
        /**
         * Convierte una imagen a base64
         * @param image la imagen a convertir
         * @return La cadena de caracteres del drawable en base64
         */

            /*
            val bitmap = (image as BitmapDrawable).bitmap
            val outputStream = ByteArrayOutputStream()
            if (bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, outputStream)) {
                return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.NO_WRAP)
            }
            throw FailedCompressionException("Ha ocurrido un error al comprimir la imagen")
        }
         */
            fun convertImageToBase64(drawable: Drawable): String {
                // Convertir a bitmap si es BitmapDrawable
                val bitmap = when (drawable) {
                    is BitmapDrawable -> drawable.bitmap
                    else -> {
                        // Crear bitmap desde cualquier drawable
                        val width = drawable.intrinsicWidth
                        val height = drawable.intrinsicHeight
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)
                        drawable.setBounds(0, 0, canvas.width, canvas.height)
                        drawable.draw(canvas)
                        bitmap
                    }
                }

                // Redimensionar si es necesario (las imágenes grandes causan problemas)
                val maxSize = 800
                val scaledBitmap = if (bitmap.width > maxSize || bitmap.height > maxSize) {
                    val ratio = maxSize.toFloat() / maxOf(bitmap.width, bitmap.height)
                    bitmap.scale(
                        (bitmap.width * ratio).toInt(),
                        (bitmap.height * ratio).toInt()
                    )
                } else {
                    bitmap
                }

                // Comprimir y convertir
                val outputStream = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

                // NO_WRAP es crucial - evita saltos de línea en el base64
                return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
            }
}