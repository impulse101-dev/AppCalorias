package com.example.appcalorias.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile   //los cambios en la variable seran visibles para todos los hilos
    private var instance: AppCaloriesDB? = null

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