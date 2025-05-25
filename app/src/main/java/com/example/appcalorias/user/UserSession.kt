package com.example.appcalorias.user

import android.content.Context
import com.example.appcalorias.db.DatabaseProvider
import com.example.appcalorias.db.model.User
import kotlinx.coroutines.runBlocking

object UserSession {
    //todo MUCHO CODIGO DE UNA Y MUY POCO PROBADO....
    private var currentUser : User? = null

    /**
     * Obtiene el usuario actual de la sesión.
     */
    val getUser : User? get() = currentUser

    /**
     * Establece el usuario actual de la sesión y lo guarda en las preferencias compartidas.
     * @param user El usuario a establecer como actual.
     * @param context El contexto de la aplicación para acceder a las preferencias compartidas.
     */
    fun setCurrentUser(user: User, context: Context) {
        currentUser = user

        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("current_user_id", user.id)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    fun logout() {
        currentUser = null
    }

    fun initialize(context : Context) {
        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("current_user_id", -1)

        if (userId != -1) {
            //carga el usuario desde room
            runBlocking {
                val userDao = DatabaseProvider.getDatabase(context).getUserDAO()
                currentUser = userDao.getUserById(userId)
            }
        }
    }

    fun logout (context: Context) {
        currentUser = null

        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("current_user_id")
            apply()
        }
    }
}