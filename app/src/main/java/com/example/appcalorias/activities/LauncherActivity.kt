package com.example.appcalorias.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.databinding.ActivityAddEditProfileBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.res.Gender
import com.example.appcalorias.db.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Actividad que se inicia al abrir la aplicacion.
 * En caso de que sea la primera vez que se ejecuta en el dispositivo, el usuario debe crear un perfil.
 * Si ya existe un perfil, se redirige con dicho perfil a la actividad principal.
 */
class LauncherActivity : AppCompatActivity() {

    private lateinit var b : ActivityAddEditProfileBinding
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var room : AppCaloriesDB

    private var user : User? = null

    //private val prefsManager : PreferencesManager = PreferencesManager.getInstance(this)

    private val ivInsertUser : ImageView by lazy { b.ivUpdateProfile }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityAddEditProfileBinding.inflate(layoutInflater)

        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initProperties()
    }

    private fun initProperties () {
        room = DatabaseProvider.getDatabase(this)

        scope.launch {
            user = User.getLastUser(applicationContext)
            println("user in launcher: $user")
            if (user == null) {
                ivInsertUser.setOnClickListener {
                    addProfile()
                }
            } else {
                changeActivity(user!!)   //user no sera nulo al pasar el if()
            }

        }
    }

    /**
     * Actividad que se encarga de pasar a la actividad principal.
     * Se pide el usuario para que no se pueda pasar un valor nulo en el id de este.
     * @param user Usuario no nulo a pasar a la siguiente actividad.
     */
    private fun changeActivity (user: User) {
        val intent = Intent(this@LauncherActivity, MainActivity::class.java)
        intent.putExtra(User.PREFS_USER_ID, user)
        startActivity(intent)
        finish()
    }


    /**
     * Agrega un nuevo perfil a la base de datos con los datos de los campos de la vista.
     */
    private fun addProfile () {
        if (!validateFields()) {
            runOnUiThread {
                Toast.makeText(this, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }
            return
        }

        val age = b.etAge.text.toString().toInt()
        val height = b.etHeight.text.toString().toInt()
        val weight = b.etWeight.text.toString().toInt()
        val gender = if (b.chipMale.isChecked) Gender.MALE else Gender.FEMALE   //uno de los 2 chips ha de ser seleccionado para llegar aqui

        val user = User(
            age = age,
            height = height,
            weight = weight,
            gender = gender
        )

        CoroutineScope(Dispatchers.IO).launch {
            val userDao = room.getUserDAO()

            userDao.insertUser(user)
            changeActivity(user)
        }
    }


    /**
     * Este metodo se encarga de validar los campos del perfil.
     * @return true si los campos son correctos, false si no lo son (faltan valores).
     */
    private fun validateFields() : Boolean {    //todo estas repitiendo codigo aqui
        return (
                b.etAge.text.toString().isNotBlank() &&   //ya no salta el NumberFormatException
                        b.etAge.text.toString().toInt() > 0 &&
                        b.etAge.text.toString().toInt() < 140 &&
                        (b.chipMale.isChecked || b.chipFemale.isChecked) &&
                        b.etHeight.text.toString().isNotBlank() &&
                        b.etHeight.text.toString().toInt() > 0 &&
                        b.etHeight.text.toString().toInt() < 300 &&
                        b.etWeight.text.toString().isNotBlank() &&
                        b.etWeight.text.toString().toInt() > 0 &&
                        b.etWeight.text.toString().toInt() < 500
                )
    }


    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

}