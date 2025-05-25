package com.example.appcalorias.activities

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
import com.example.appcalorias.databinding.ActivityAddEditProfileBinding
import com.example.appcalorias.db.AppCaloriesDB
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.Gender
import com.example.appcalorias.db.model.User
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//https://www.calculator.net/bmi-calculator.html !!!!!!!!!!!!!!

class AddEditProfile : AppCompatActivity() {


    private lateinit var b : ActivityAddEditProfileBinding

    private val chipMale : Chip by lazy { b.chipMale }
    private val chipFemale : Chip by lazy { b.chipFemale }
    private val etAge : EditText by lazy { b.etAge }
    private val etHeight : EditText by lazy { b.etHeight }
    private val etWeight : EditText by lazy { b.etWeight }
    private val ivUpdateProfile : ImageView by lazy { b.ivUpdateProfile }
//    private val ivAddProfile : ImageView by lazy { b.ivSaveNewProfile }
//    private val etName : EditText by lazy { b.etName }

    private lateinit var room : AppCaloriesDB
    //private lateinit var userImagePicker: ImagePickerManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityAddEditProfileBinding.inflate(layoutInflater)
        setSupportActionBar(b.toolBar)

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
//        userImagePicker = ImagePickerManager(
//            this,
//            ivUserPhoto
//        )
        initActionListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.miAddProfile -> {
//                Toast.makeText(this, "Ya estas en esta pantalla", Toast.LENGTH_SHORT).show()
//            }

            R.id.miProfile -> {
                Toast.makeText(this, "Ya estas en esta pantalla", Toast.LENGTH_SHORT).show()
            }

            R.id.miCalendar -> {

            }
        }
        return true
    }

    private fun initActionListeners () {

        ivUpdateProfile.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //todo AQUI ACTUALIZA EL PERFIL QUE YA EXISTE MY NAME!!!
            }

        }

//        ivUserPhoto.setOnClickListener {
//            userImagePicker.pickImage()
//        }
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

        val age = etAge.text.toString().toInt()
        val height = etHeight.text.toString().toInt()
        val weight = etWeight.text.toString().toInt()
        val gender = if (chipMale.isChecked) Gender.MALE else Gender.FEMALE   //uno de los 2 chips ha de ser seleccionado para llegar aqui

        val user = User(
            age = age,
            //name = etName.text.toString(),
            height = height,
            weight = weight,
            gender = gender,
            //image = "",
            bmr = calculateBMR(
                age, gender, height, weight
            )
        )

        CoroutineScope(Dispatchers.IO).launch {
            val userDao = room.getUserDAO()

            userDao.insertUser(user)
            finish()
            println("tras el delete: ${userDao.getAll()}, size: ${userDao.getAll().size}")
        }
        //Toast.makeText(this, "Usuario agregado", Toast.LENGTH_SHORT).show()
    }

    /**
     * Este metodo se encarga de validar los campos del perfil.
     * @return true si los campos son correctos, false si no lo son (faltan valores).
     */
    private fun validateFields() : Boolean {
        return (
                //etName.text.toString().isNotBlank() &&
                        etAge.text.toString().isNotBlank() &&   //ya no salta el NumberFormatException
                        etAge.text.toString().toInt() > 0 &&
                        etAge.text.toString().toInt() < 140 &&
                        (chipMale.isChecked || chipFemale.isChecked) &&
                        etHeight.text.toString().isNotBlank() &&
                        etHeight.text.toString().toInt() > 0 &&
                        etHeight.text.toString().toInt() < 300 &&
                        etWeight.text.toString().isNotBlank() &&
                        etWeight.text.toString().toInt() > 0 &&
                        etWeight.text.toString().toInt() < 500
                )
    }


    /**
     * Este metodo se encarga de calcular el BMR (Basal Metabolic Rate) del usuario.
     * @param age la edad del usuario.
     * @param gender el genero del usuario.
     * @param height la altura del usuario.
     * @param weight el peso del usuario.
     * @return el BMR del usuario formateado a Int.
     */
    private fun calculateBMR (age: Int, gender: Gender, height: Int, weight: Int) : Int {
        return if (gender == Gender.MALE)
            ((10 * weight) + (6.25 * height) - (5 * age) + 5).toInt()
        else
            ((10 * weight) + (6.25 * height) - (5 * age) - 161).toInt()
    }

}