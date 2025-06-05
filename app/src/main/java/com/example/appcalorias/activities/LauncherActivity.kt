package com.example.appcalorias.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.databinding.ActivityAddEditProfileBinding
import com.example.appcalorias.databinding.ActivitySplashBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.res.Gender
import com.example.appcalorias.db.model.User
import com.example.appcalorias.db.model.res.DateUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Actividad que se inicia al abrir la aplicacion.
 * En caso de que sea la primera vez que se ejecuta en el dispositivo, el usuario debe crear un perfil.
 * Si ya existe un perfil, se redirige con dicho perfil a la actividad principal.
 * @property splashBinding Vista que se muestra mientras se carga la aplicacion.
 * @property scope Scope de corutinas para manejar operaciones asincronas.
 * @property room Singleton para el acceso a la base de datos.
 * @property user Usuario que se carga desde la base de datos si ya existe.
 * @author Adrian Salazar Escoriza
 */
class LauncherActivity : AppCompatActivity() {

    private lateinit var b : ActivityAddEditProfileBinding
    private lateinit var splashBinding : ActivitySplashBinding
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var room : AppCaloriesDB

    private var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        b = ActivityAddEditProfileBinding.inflate(layoutInflater)

        //setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initProperties()
    }

    /**
     * Inicializa las propiedades necesarias para la actividad.
     */
    private fun initProperties () {
        room = DatabaseProvider.getDatabase(this)
        initActionListeners()

        scope.launch {
            delay(1000)  //para que se vea el loadingDialog

            withContext(Dispatchers.Main) {
                user = room.getUserDAO().getLastUser()
            }


            if (user == null) {
                setContentView(b.root)
                b.btnUpdateProfile.setOnClickListener {
                    addProfile()
                }
            } else {
                changeActivity(user!!)   //user no sera nulo al pasar el if()
            }

        }
    }

    private fun initActionListeners () {
        b.etAge.setOnClickListener {
            b.etAge.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    b.etAge.setText(formattedDate)
                }, year, month, day)

                datePickerDialog.show()
            }
        }
    }

    /**
     * Actividad que se encarga de pasar un usuario a la actividad principal.
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

        val age = DateUpdate.getCurrentAge(b.etAge.text.toString())
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
            changeActivity(userDao.getLastUser()!!)
        }
    }


    /**
     * Este metodo se encarga de validar los campos del perfil.
     * @return true si los campos son correctos, false si no lo son (faltan valores).
     */
    private fun validateFields() : Boolean {
        return (
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