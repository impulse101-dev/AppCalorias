package com.example.appcalorias.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.R
import com.example.appcalorias.api.ApiUtilities
import com.example.appcalorias.api.response.post.foodProperties.FoodPropertiesManager
import com.example.appcalorias.config.ConfigLoader
import com.example.appcalorias.databinding.ActivityMainBinding
import com.example.appcalorias.db.model.User
import com.example.appcalorias.image.ImageConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    //    val btPrueba: Button by lazy { findViewById(R.id.btProbarModelo) }
//    val etUserPrompt: EditText by lazy { findViewById(R.id.eTuserPrompt) }
    private val ivSendRequest: ImageView by lazy { b.ivSendPetition }
    private val ivPhoto: ImageView by lazy { b.ivPhoto }
    private val toolBar : Toolbar by lazy { b.toolBar }

    /**
     * Variable que contiene el usuario logueado
     */
    var userSelected : User? = null

    /**
     * Variable que indica si el usuario ha seleccionado alguna imagen
     */
    private var hasImage: Boolean = false
    private val pickMedia =
        registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri == null) {
                Log.d("PickVisualMedia", "No se ha seleccionado nada")
                Toast.makeText(
                    this,
                    "No ha seleccionado ninguna imagen",
                    Toast.LENGTH_SHORT
                )
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

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Calories Estimator"          //todo haz un ToolBarManager para no estar repitiendo el codigo

        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ConfigLoader.init(this)

        initActionListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miAddProfile -> {
                Intent(this, AddEditProfile::class.java).also{ startActivity(it) }
            }

            R.id.miCalendar -> {

            }
        }
        return true
    }

    /**
     * Inicializa los listeners de los botones.
     */
    private fun initActionListeners() {
        ivPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }


        ivSendRequest.setOnClickListener {

            if (!hasImage) {
                Toast.makeText(this, "Debe de utilizar una imagen", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "Procesando imagen...", Toast.LENGTH_SHORT).show()

                /*
               todo
                1.- PONER EN UN TRY CATCH EL INDEX OUT OF BOUNDS Y QUE VUELVA A HACER UNA PETICION A LA API (probar)    no creo que haga falta
                2.- Hacer que mientras cargue la respuesta de la api, que salga un loading
                3.- Poner la vista mas bonita
                4.- ADRIAN, HAS QUITADO LAS GRASAS DEL PROMPT, CUIDADO POR SI DA POR CULO... JDKSLAJDLKASJDSKLA     hecho
                5.- Documentar las clases cuando termines de hacer el to do
                6.- Esto a lo mejor es mucho, pero estaria bien hacer una base de datos con sqlite para guardar las calorias que se van consumiendo (hacer extremadamente sencilla)
                para el apartado 6, necesitaria entonces hacer su vista... siendo asi bastante mas trabajo... pero bueno    HACER CON ROOM (VIDEO DIEGO MOODLE)
                7.- Al no subir una foto, en lugar de usar el Toast, poner algo que sea mas bonico
             */


                CoroutineScope(Dispatchers.IO).launch {

                    val call = ApiUtilities.postPrompt(
                        ImageConverter.convertImageToBase64(ivPhoto.drawable)
                    )

                    runOnUiThread {
                        if (call.isSuccessful) {

                            FoodPropertiesDialog(
                                FoodPropertiesManager(call.body()!!).getFoodProperties()
                            ).show(
                                (b.root.context as AppCompatActivity).supportFragmentManager,
                                "FoodPropertiesDialog"
                            )
                            Log.d(
                                "ResultadoFoodProperties",
                                FoodPropertiesManager(call.body()!!).getFoodProperties().toString()
                            )

//                            val contenido = call.body()
//                            if (contenido != null) {
//                                println("Contenido: $contenido")
//                            } else {
//                                println("Error al cargar los modelos")
//                            }
                        } else {
                            println("Error en la llamada a la API\nCodigo de error: ${call.code()}")
                        }
                    }

                }

            }
        }
    }
}