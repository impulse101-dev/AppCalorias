package com.example.appcalorias

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcalorias.databinding.ActivityConfiguracionPerfilBinding
//https://www.calculator.net/bmi-calculator.html !!!!!!!!!!!!!!

//todo TE HAS QUEDADO POR AQUI
class ConfiguracionPerfil : AppCompatActivity() {


    private lateinit var b : ActivityConfiguracionPerfilBinding

    private val switchGender : SwitchCompat by lazy { b.switchGender }
    private val etAge : EditText by lazy { b.etAge }
    private val etHeight : EditText by lazy { b.etHeight }
    private val etWeight : EditText by lazy { b.etWeight }
    private val ivUpdateProfile : ImageView by lazy { b.ivUpdateProfile }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        b = ActivityConfiguracionPerfilBinding.inflate(layoutInflater)

        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Este metodo se encarga de devolver si los valores son aceptables para el perfil o no.
     * @return true si los valores son aceptables, false si no lo son (faltan valores).
     */
    fun hasOkValues () : Boolean {
        return false
    }

}