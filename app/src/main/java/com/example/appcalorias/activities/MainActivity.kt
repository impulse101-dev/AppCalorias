package com.example.appcalorias.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.R
import com.example.appcalorias.api.ollama.ApiUtilities
import com.example.appcalorias.api.ollama.response.post.foodProperties.FoodPropertiesManager
import com.example.appcalorias.api.ollama.config.ConfigLoader
import com.example.appcalorias.databinding.ActivityMainBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.Record
import com.example.appcalorias.db.model.User
import com.example.appcalorias.image.ImageConverter
import com.example.appcalorias.image.ImagePickerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    private val ivSendRequest: ImageView by lazy { b.ivSendPetition }
    private val ivPhoto: ImageView by lazy { b.ivPhoto }


    /**
     * Variable que indica si el usuario ha seleccionado alguna imagen
     */
    private var hasImage: Boolean = false
    private lateinit var imagePicker : ImagePickerManager

    private var user : User? = null
    private lateinit var room : AppCaloriesDB

    private var runningModel : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolBar)
        supportActionBar?.title = "Calories Estimator"

        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ConfigLoader.init(this)

        initProperties()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miCalendar -> {
                CoroutineScope(Dispatchers.IO).launch {
                    println("recordsForUser?: ${room.getRecordDAO().getRecordsByUserId(user!!.id)}")
                }

                val intent = Intent(this, RecordListActivity::class.java)
                intent.putExtra(User.PREFS_USER_ID, user)
                startActivity(intent)
                //finish()

            }

            R.id.miProfile -> {
                Intent(this, AddEditProfileActivity::class.java).also{ startActivity(it) }
            }
        }
        return true
    }

    private fun initProperties () {
        room = DatabaseProvider.getDatabase(this)
        user = intent.getSerializableExtra(User.PREFS_USER_ID, User::class.java)        //cambiado el minSdkVersion a 33 para poder usar el nuevo metodo de Serializable
        imagePicker = ImagePickerManager(this, autoLoadInto = ivPhoto)

        //println("Main, tras iniciar properties - user: $user")

        initActionListeners()
    }

    /**
     * Inicializa los listeners de los botones.
     */
    private fun initActionListeners() {
        ivPhoto.setOnClickListener {
            imagePicker.pickImage()
        }


        ivSendRequest.setOnClickListener {

            if (imagePicker.hasImage()) {
                hasImage = true     //al haber seleccionado ya una imagen, basta
            }

            if (!hasImage) {
                Toast.makeText(this, "Debe de utilizar una imagen", Toast.LENGTH_SHORT).show()

            } else if (runningModel) {
                Toast.makeText(this, "Una peticion ya se esta ejecutando", Toast.LENGTH_SHORT).show()

            } else if (user == null) {
                Toast.makeText(this, "ERROR, NO HAY USUARIOS", Toast.LENGTH_SHORT).show()

            } else {

                Toast.makeText(this, "Procesando imagen...", Toast.LENGTH_SHORT).show()

                CoroutineScope(Dispatchers.IO).launch {

                    runningModel = true

                    val call = ApiUtilities.postPrompt(
                        ImageConverter.convertImageToBase64(ivPhoto.drawable)
                    )

                    runOnUiThread {
                        if (call.isSuccessful) {

                            val foodProperties = FoodPropertiesManager(
                                call.body()!!
                            ).getFoodProperties()

                            FoodPropertiesDialog(
                                foodProperties,
                                onFoodPropertiesAccepted = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        room.getRecordDAO().insertRecord(
                                            Record(
                                                calories = foodProperties.calories.toFloat(),
                                                carbohydrates = foodProperties.carbohydrates.toInt(),
                                                proteins = foodProperties.protein.toInt(),
                                                fats = foodProperties.fat.toInt(),
                                                idUser = user!!.id
                                            )
                                        )
                                    }
                                }
                            ).show(
                                (b.root.context as AppCompatActivity).supportFragmentManager,
                                "FoodPropertiesDialog"
                            )

                            runningModel = false

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