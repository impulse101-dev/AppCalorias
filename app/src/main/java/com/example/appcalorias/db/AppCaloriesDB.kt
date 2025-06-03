package com.example.appcalorias.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcalorias.db.dao.RecordDAO
import com.example.appcalorias.db.dao.UserDAO
import com.example.appcalorias.db.model.User
import com.example.appcalorias.db.model.Record

/**
 * Clase que representa la base de datos de la aplicación con Room.
 * Contiene las entidades User y Record, y sus respectivos DAOs.
 * @author Adrian Salazar Escoriza
 */
@Database(
    entities = [User::class, Record::class],
    version = 11
)
abstract class AppCaloriesDB : RoomDatabase() {
    /**
     * Obtiene el DAO para manejar las operaciones de la tabla User.
     * @return UserDAO
     */
    abstract fun getUserDAO() : UserDAO

    /**
     * Obtiene el DAO para manejar las operaciones de la tabla Record.
     * @return RecordDAO
     */
    abstract fun getRecordDAO() : RecordDAO
}