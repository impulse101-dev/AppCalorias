package com.example.appcalorias

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.api.ApiUtilities
import com.example.appcalorias.config.ConfigLoader
import com.example.appcalorias.image.ImageConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {
    val btPrueba: Button by lazy { findViewById(R.id.btProbarModelo) }
    val etUserPrompt: EditText by lazy { findViewById(R.id.eTuserPrompt) }
    private val btCamera: Button by lazy { findViewById(R.id.btCamera) }
    private val ivPhoto: ImageView by lazy { findViewById(R.id.ivPhoto) }

    /**
     * Variable que indica si el usuario ha seleccionado alguna imagen
     */
    private var hasImage: Boolean = false
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri == null) {
                Log.d("PickVisualMedia", "No se ha seleccionado nada")
//            Toast.makeText(
//                this,
//                "No ha seleccionado ninguna imagen",
//                Toast.LENGTH_SHORT
//            )
            } else {
                Log.d("PickVisualMedia", "Se ha seleccionado: $uri")
                ivPhoto.setImageURI(uri)
                hasImage = true
            }
            Log.d("hasImage", "$hasImage")
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ConfigLoader.init(this)

        initActionListeners()

    }

    private fun initActionListeners() {
        ivPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }


        btPrueba.setOnClickListener {

            Toast.makeText(this, "Procesando imagen...", Toast.LENGTH_SHORT).show()


            CoroutineScope(Dispatchers.IO).launch {
//                val call = RetrofitClient.getApiService().
//                create(ApiService::class.java).
//                //getModelosEjecutandose("api/ps")
//                    getResponse(etUserPrompt.text.toString())

                val borrar = ImageConverter.convertImageToBase64(ivPhoto.drawable)

                Log.d("borrar", borrar)


                val call = ApiUtilities.postPrompt(
                    etUserPrompt.text.toString(),
                    borrar
                )


                //call = ApiUtilities.getRunningModels()

                runOnUiThread {
                    if (call.isSuccessful) {
                        val contenido = call.body()
                        if (contenido != null) {
                            println("Contenido: $contenido")
                        } else {
                            println("Error al cargar los modelos")
                        }
                    } else {
                        println("Error en la llamada a la API\nCodigo de error: ${call.code()}")
                    }
                }

            }

        }
    }
}