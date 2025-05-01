package com.example.appcalorias

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.api.ApiUtilities
import com.example.appcalorias.config.ConfigLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val btPrueba : Button by lazy { findViewById(R.id.btProbarModelo) }
    val etUserPrompt : EditText by lazy { findViewById(R.id.eTuserPrompt) }
    private val btCamera : Button by lazy { findViewById(R.id.btCamera) }
    private val ivPhoto : ImageView by lazy { findViewById(R.id.ivPhoto) }
//    private val pickImageLauncher  = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            ivPhoto.setImageURI(it) // Establecer la imagen seleccionada en el ImageView
//        }


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

        btCamera.setOnClickListener {


            //pickImageLauncher.launch("image/*")

            /*
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivity(intent)
            println("sucediendo lo siguiente")
            ivPhoto.setImageURI(intent.data)
             */
        }
        btPrueba.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
//                val call = RetrofitClient.getApiService().
//                create(ApiService::class.java).
//                //getModelosEjecutandose("api/ps")
//                    getResponse(etUserPrompt.text.toString())

                val call = ApiUtilities.postPrompt(etUserPrompt.text.toString())

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