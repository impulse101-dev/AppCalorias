package com.example.appcalorias.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.databinding.ActivityAddEditProfileBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.user.res.Gender
import com.example.appcalorias.db.model.user.User
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
            val users = room.getUserDAO().getAllUsers()

            Log.d("initUsuarios", "$users")

            if (users.isNotEmpty()) {
                changeActivity()
            } else {
                //no quiero que la gente le de a agregar perfil mientras carga esto (no podran al agregar una pantalla de carga)
                ivInsertUser.setOnClickListener {
                    addProfile()
                }
            }
        }
    }

    private fun changeActivity () {
        startActivity(
            Intent(this@LauncherActivity, MainActivity::class.java).also { finish() }
        )
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
            changeActivity()
            //println("tras el delete: ${userDao.getAll()}, size: ${userDao.getAll().size}")
        }
        //Toast.makeText(this, "Usuario agregado", Toast.LENGTH_SHORT).show()
    }


    /**
     * Este metodo se encarga de validar los campos del perfil.
     * @return true si los campos son correctos, false si no lo son (faltan valores).
     */
    private fun validateFields() : Boolean {    //todo estas repitiendo codigo aqui... Es para avanzar rapido por ahora
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