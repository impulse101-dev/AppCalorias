package com.example.appcalorias.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.R
import com.example.appcalorias.activities.LauncherActivity
import com.example.appcalorias.activities.res.toolbar.ToolbarManager
import com.example.appcalorias.databinding.ActivityAddEditProfileBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.res.Gender
import com.example.appcalorias.db.model.User
import com.example.appcalorias.db.model.res.DateUpdate
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//https://www.calculator.net/bmi-calculator.html !!!!!!!!!!!!!!


/**
 * Actividad encargada de modificar el perfil del usuario
 * @property room Singleton para el acceso a la base de datos.
 * @property user Usuario con el que se muestran los hints de los campos.
 * @property toolbarManager Encargado de gestionar la toolbar de la actividad.
 * @author Adrian Salazar Escoriza
 */
class AddEditProfileActivity : AppCompatActivity() {


    private lateinit var b: ActivityAddEditProfileBinding
    private lateinit var room: AppCaloriesDB
    private var user: User? = null
    private lateinit var toolbarManager: ToolbarManager


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

    /**
     * Inicializa las propiedades de la actividad.
     */
    private fun initProperties() {
        toolbarManager = ToolbarManager(this, b.toolBar)
        room = DatabaseProvider.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            user = room.getUserDAO().getLastUser()
            toolbarManager.setup()
            withContext(Dispatchers.Main) {
                if (user != null) {
                    setActualUserProperties(user!!)
                }   //si no hay usuario que no ponga los hints
            }
        }

        initActionListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return toolbarManager.createMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return toolbarManager.handleItemClick(item)
    }

    /**
     * Inicializa los listeners de los botones de la actividad.
     */
    private fun initActionListeners() {
        b.btnUpdateProfile.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                addProfile()
            }
        }

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

    /**
     * Actividad que se encarga de pasar un usuario a la actividad principal.
     * Se pide el usuario para que no se pueda pasar un valor nulo en el id de este.
     * @param user Usuario no nulo a pasar a la siguiente actividad.
     */
    private fun changeActivity(user: User) {
        val intent = Intent(this@AddEditProfileActivity, MainActivity::class.java)
        intent.putExtra(User.PREFS_USER_ID, user)
        startActivity(intent)
        finish()
    }

    /**
     * Agrega un nuevo perfil a la base de datos con los datos de los campos de la vista.
     */
    private suspend fun addProfile() {
        if (!validateFields()) {
            runOnUiThread {
                Toast.makeText(this, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }
            return
        }

        //si el usuario principal no tiene registros y agregamos otro, borrar el viejo.
        CoroutineScope(Dispatchers.IO).launch {
            if (
                user != null &&
                room.getRecordDAO().getRecordsByUserId(user!!.id).isEmpty()
                ) {
                room.getUserDAO().deleteUser(user!!)
            }
        }

        val age = DateUpdate.getCurrentAge(b.etAge.text.toString())
        val height = b.etHeight.text.toString().toInt()
        val weight = b.etWeight.text.toString().toInt()
        val gender =
            if (b.chipMale.isChecked) Gender.MALE else Gender.FEMALE   //uno de los 2 chips ha de ser seleccionado para llegar aqui

        val user = User(
            age = age,
            height = height,
            weight = weight,
            gender = gender,
        )

        val userDao = room.getUserDAO()

        userDao.insertUser(user)

        this.user = userDao.getLastUser()

        changeActivity(this.user!!)
    }

    /**
     * Este metodo se encarga de validar los campos del perfil.
     * @return true si los campos son correctos, false si no lo son (faltan valores).
     */
    fun validateFields(): Boolean {
        return (
                        (b.chipMale.isChecked || b.chipFemale.isChecked) &&
                        b.etHeight.text.toString().isNotBlank() &&
                        b.etHeight.text.toString().toInt() > 0 &&
                        b.etHeight.text.toString().toInt() < 300 &&
                        b.etWeight.text.toString().isNotBlank() &&
                        b.etWeight.text.toString().toInt() > 0 &&
                        b.etWeight.text.toString().toInt() < 500
                )
    }

    /**
     * Este metodo se encarga de establecer los valores actuales del usuario en los campos de la vista.
     * @param user Usuario del que se quieren establecer los valores.
     */
    private fun setActualUserProperties(user: User) {
        b.etAge.hint = user.age.toString()
        b.etWeight.hint = user.weight.toString()
        b.etHeight.hint = user.height.toString()
        if (user.gender == Gender.MALE) b.chipMale.isChecked = true else {
            b.chipFemale.isChecked = true
        }
    }


}