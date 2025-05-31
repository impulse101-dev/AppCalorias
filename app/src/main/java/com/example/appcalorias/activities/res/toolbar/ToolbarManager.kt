package com.example.appcalorias.activities.res.toolbar

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.appcalorias.R
import com.example.appcalorias.activities.AddEditProfileActivity
import com.example.appcalorias.activities.UserSelectionActivity

/**
 * Clase encargada de gestionar la barra de herramientas personalizada.
 * Esta se encarga de aplicar el funcionamiento de cada uno de los items del menu de la toolbar, para
 * las actividades.
 * @param activity Actividad a la que se le aplica la toolbar.
 * @param toolBar Toolbar personalizada que se va a utilizar en la actividad.
 * @author Adrian Salazar Escoriza
 */
class ToolbarManager
    (
    val activity: AppCompatActivity,
    private val toolBar: Toolbar,
) {
    /**
     * Configura la toolbar personalizada en la actividad.
     * (Realiza el .setSupportActionBar(toolBar) de la actividad)
     */
    fun setup() {
        activity.setSupportActionBar(toolBar)
    }

    /**
     * Crea el menú de la toolbar personalizada.
     * @param menu Menú al que se le va a aplicar el menú personalizado.
     */
    fun createMenu(menu: Menu?): Boolean {
        activity.menuInflater.inflate(R.menu.custom_main_toolbar, menu)
        activity.setTitle("App Calorias")
        return true
    }

    /**
     * Maneja el evento de clic en los items del menú de la toolbar personalizada.
     * @param itemMenu Item del menú que se ha pulsado.
     */
    fun handleItemClick(itemMenu: MenuItem): Boolean {
        when (itemMenu.itemId) {
            R.id.miProfile -> {
                if (activity !is AddEditProfileActivity) {
                    navigateToActivity(AddEditProfileActivity::class.java)
                }
            }

            R.id.miCalendar -> {
                if (activity !is UserSelectionActivity) {
                    navigateToActivity(UserSelectionActivity::class.java)
                }
            }
        }
        return true
    }

    /**
     * Navega a la actividad especificada.
     * @param activityClass Clase de la actividad a la que se quiere navegar.
     */
    private fun <T : AppCompatActivity> navigateToActivity (activityClass : Class<T>) {
        val intent = Intent(activity, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}