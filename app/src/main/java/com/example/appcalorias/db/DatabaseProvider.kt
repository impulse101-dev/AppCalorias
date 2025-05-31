package com.example.appcalorias.db

import android.content.Context
import androidx.room.Room

/**
 * Singleton que proporciona acceso a la base de datos de Room.
 * @property instance Instancia de la base de datos. Se inicializa en el metodo [getDatabase].
 * @see getDatabase
 * @author Adrian Salazar Escoriza
 */
object DatabaseProvider {
    @Volatile   //los cambios en la variable seran visibles para todos los hilos
    private var instance: AppCaloriesDB? = null

    /**
     * Obtiene la instancia de la base de datos. Si no existe, la crea.
     * @param context Contexto de la aplicacion, necesario para crear la base de datos.
     * @return Instancia de la base de datos AppCaloriesDB.
     */
    fun getDatabase(context: Context) : AppCaloriesDB {
        return instance ?: synchronized(this) {
            val aux = Room.databaseBuilder(
                context = context,
                klass = AppCaloriesDB::class.java,
                name = "app_calories_database"
            ).fallbackToDestructiveMigration(true)  //cada vez que se hagan cambios en la version, se borraran los datos
                .build()
            instance = aux
            instance!!
        }
    }
}