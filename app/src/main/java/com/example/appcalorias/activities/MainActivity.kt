package com.example.appcalorias.activities

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.activities.dialogs.FoodPropertiesDialog
import com.example.appcalorias.activities.dialogs.LoadingDialog
import com.example.appcalorias.activities.res.toolbar.ToolbarManager
import com.example.appcalorias.api.ollama.ApiUtilities
import com.example.appcalorias.api.ollama.response.post.foodProperties.FoodPropertiesManager
import com.example.appcalorias.api.ollama.config.ConfigLoader
import com.example.appcalorias.databinding.ActivityMainBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.Record
import com.example.appcalorias.db.model.User
import com.example.appcalorias.image.ImageConverter
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    private val ivSendRequest: ImageView by lazy { b.ivSendPetition }


    /**
     * Variable que indica si el usuario ha seleccionado alguna imagen
     */
    private var hasImage: Boolean = false

    private var user : User? = null
    private lateinit var room : AppCaloriesDB

    private var runningModel : Boolean = false

    private lateinit var toolbarManager : ToolbarManager

    val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            hasImage = true
            Log.d("MainActivity", "Imagen seleccionada: $uri")
            Picasso.get().load(uri).fit().centerCrop().into(b.ivPhoto)
        } else {
            Log.d("MainActivity", "Ninguna imagen seleccionada")
        }
    }

    override fun onResume() {
        /*
        En caso de volver de una actividad donde hemos creado un nuevo usuario, el usuario del main
        no se esta actualizando, por ello lo vuelvo a cargar desde la base de datos directamente.
        Y desde el main, que vuelva a pasar a las actividades.
         */
        CoroutineScope(Dispatchers.Main).launch {
            user = room.getUserDAO().getLastUser()
        }
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ConfigLoader.init(this)

        initProperties()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return toolbarManager.createMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toolbarManager.handleItemClick(item)
    }

    private fun initProperties () {
        Picasso.
        get().
        load("https://cdn.pixabay.com/photo/2021/02/08/12/40/lasagna-5994612_960_720.jpg").
        fit().
        centerCrop().
        into(b.ivPhoto)

        room = DatabaseProvider.getDatabase(this)
        user = intent.getSerializableExtra(User.PREFS_USER_ID, User::class.java)        //cambiado el minSdkVersion a 33 para poder usar el nuevo metodo de Serializable
        toolbarManager = ToolbarManager(this, b.toolBar)
        toolbarManager.setup()

        initActionListeners()
    }

    /**
     * Inicializa los listeners de los botones.
     */
    private fun initActionListeners() {
        //https://developer.android.com/training/data-storage/shared/photopicker?hl=es-419
        b.btGalery.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }


        ivSendRequest.setOnClickListener {

            println("Usuario en el main: $user")
            CoroutineScope(Dispatchers.Main).launch {
                println("Ultimo usuario de la base de datos: ${room.getUserDAO().getLastUser()}")
            }

            if (!hasImage) {
                Toast.makeText(this, "Debe de utilizar una imagen", Toast.LENGTH_SHORT).show()

            } else if (runningModel) {
                Toast.makeText(this, "Una peticion ya se esta ejecutando", Toast.LENGTH_SHORT).show()

            } else if (user == null) {
                Toast.makeText(this, "ERROR, NO HAY USUARIOS", Toast.LENGTH_SHORT).show()
            } else {

                //Toast.makeText(this, "Procesando imagen...", Toast.LENGTH_SHORT).show()

                CoroutineScope(Dispatchers.IO).launch {
                    val loadingDialog = LoadingDialog()
                    loadingDialog.show(supportFragmentManager, "loadingDialog")

                    runningModel = true

                    try {
                        val call = ApiUtilities.postPrompt(
                            ImageConverter.convertImageToBase64(b.ivPhoto.drawable)
                        )

                        runOnUiThread {
                            loadingDialog.dismiss()

                            if (call.isSuccessful) {

                                val foodProperties = FoodPropertiesManager(
                                    call.body()!!
                                ).getFoodProperties()

                                FoodPropertiesDialog(
                                    foodProperties,
                                    onFoodPropertiesAccepted = {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            room.getRecordDAO().insertRecord(
                                                Record(
                                                    calories = foodProperties.calories.toFloat(),
                                                    carbohydrates = foodProperties.carbohydrates.toFloat(),
                                                    proteins = foodProperties.protein.toFloat(),
                                                    fats = foodProperties.fat.toFloat(),
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

                            } else {
                                println("Error en la llamada a la API\nCodigo de error: ${call.code()}")
                                runningModel = false
                            }
                        }

                    } catch (e : Exception) {
                        runOnUiThread {
                            loadingDialog.dismiss()
                        }
                    }


                }

            }
        }
    }
}