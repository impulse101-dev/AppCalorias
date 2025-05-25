package com.example.appcalorias.image

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.squareup.picasso.Picasso


/**
 * Clase utilitaria que encapsula la funcionalidad para seleccionar imágenes
 * desde la galería del dispositivo. Esta clase maneja automáticamente toda la
 * lógica relacionada con los contratos de selección de contenido visual de Android.
 *
 * Puede ser utilizada tanto en Activities como en Fragments, y permite
 * configurar callbacks personalizados para procesar la imagen seleccionada.
 *
 * @property host El contexto donde se utilizará el selector de imágenes (Activity o Fragment)
 * @property onImageSelected Callback opcional que se invoca cuando se selecciona una imagen
 * @property autoLoadInto ImageView opcional donde se cargará automáticamente la imagen seleccionada
 *
 * @throws IllegalArgumentException Si el host no es una instancia válida de AppCompatActivity o Fragment
 *
 * @author Adrian Salazar Escoriza
 */

class ImagePickerManager(
    private val host : Any,
    private val autoLoadInto: ImageView
    )
{
    /** Propiedad que indica si el usuario ha seleccionado una imagen, propiedad que puede variar dentro de la clase*/
    private var pHasImage = false

    /** Lanzador de actividad para seleccionar imagenes */
    private val pickMedia : ActivityResultLauncher<PickVisualMediaRequest>

    private val onImageSelected : ((Uri?) -> Unit)? = null


    /**
     * Inicializar de la clase. Configura el launcher adecuado de la actividad segun
     * el tipo de host (Activity o Fragment).
     */
    init {

        require(host is AppCompatActivity || host is Fragment) {
            "El host debe ser una instancia de AppCompatActivity o Fragment"
        }

        pickMedia = when (host) {
            is AppCompatActivity -> {
                host.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
                    handleImageResult(uri, host)
                }
            }
            is Fragment -> {
                host.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
                    handleImageResult(uri, host.requireActivity())
                }
            }
            else -> throw IllegalArgumentException("Tipo de host no soportado")
        }

    }

    /**
     * Procesa el resultado de la selección de imagen.
     * En caso de que sea nulo, se muestra un Toast informando que no se ha seleccionado
     * ninguna imagen.
     *
     * @param uri URI de la imagen seleccionada, o null si no se seleccionó ninguna
     * @param activity Contexto de la actividad para mostrar mensajes
     */
    private fun handleImageResult (uri : Uri?, activity: FragmentActivity) {
        if (uri == null) {
            Log.d("ImagePickerManager", "No se ha seleccionado ninguna imagen")
            Toast.makeText(
                activity,
                "No ha seleccionado ninguna imagen",
                Toast.LENGTH_SHORT
            ).show()
            //pHasImage = false al ya haber seleccionado una imagen, la propiedad no deberia de volver a ser nula
        } else {
            Log.d("ImagePickerManager", "Imagen seleccionada: $uri")
            pHasImage = true
            // Cargar la imagen en el ImageView si se proporcionó
            autoLoadInto.let { imageView ->
                Picasso.get()
                    .load(uri)
                    .fit()
                    .centerCrop()
                    .into(imageView)
            }
        }

        onImageSelected?.invoke(uri)
    }


    fun pickImage () {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun hasImage () : Boolean {
        return pHasImage
    }
}