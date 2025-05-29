package com.example.appcalorias.activities.res.toolbar

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.appcalorias.R
import com.example.appcalorias.activities.AddEditProfileActivity
import com.example.appcalorias.activities.RecordListActivity
import com.example.appcalorias.db.model.User

class ToolbarManager
    (
    val activity: AppCompatActivity,
    private val toolBar: Toolbar,
    private val user: User?
) {
    fun setup() {
        activity.setSupportActionBar(toolBar)
    }

    fun createMenu(menu: Menu?): Boolean {
        activity.menuInflater.inflate(R.menu.custom_main_toolbar, menu)
        activity.setTitle("App Calorias")
        return true
    }

    fun handleItemClick(itemMenu: MenuItem): Boolean {
        when (itemMenu.itemId) {
            R.id.miProfile -> {
                if (activity !is AddEditProfileActivity) {
                    navigateToActivity(AddEditProfileActivity::class.java)
                }
            }

            R.id.miCalendar -> {
                if (activity !is RecordListActivity) {
                    navigateToActivity(RecordListActivity::class.java)
                }
            }
        }
        return true
    }

    private fun <T : AppCompatActivity> navigateToActivity (activityClass : Class<T>) {
        val intent = Intent(activity, activityClass)
        if (user != null) {
            intent.putExtra(User.PREFS_USER_ID, user)
        }
        //esta flag hace que al volver a la actividad anterior, se elimine la pila de actividades anteriores
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}