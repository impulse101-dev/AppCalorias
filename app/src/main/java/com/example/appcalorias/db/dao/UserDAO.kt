package com.example.appcalorias.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.appcalorias.db.model.User

/**
 * DAO para manejar las operaciones de la tabla User en la base de datos.
 * Proporciona métodos para insertar, eliminar y consultar usuarios.
 * @author Adrian Salazar Escoriza
 */
@Dao
interface UserDAO {

    /**
     * Obtiene el ultimo usuario insertado en la base de datos.
     * @return SELECT * FROM User ORDER BY id DESC LIMIT 1
     */
    @Query("SELECT * FROM User ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser() : User?

    /**
     * Obtiene todos los usuarios en orden descendente por ID, excluyendo al usuario puesto por parametro.
     * @param mainUserId ID del usuario principal que se desea excluir de la lista.
     * @return SELECT * FROM User WHERE id != :mainUserId ORDER BY id DESC
     */
    @Query("SELECT * FROM User WHERE id != :mainUserId ORDER BY id DESC")
    suspend fun getAllOtherUsers(mainUserId : Int) : List<User>

    /**
     * Inserta un usuario en la base de datos.
     * @param user el usuario a insertar
     */
    @Insert
    suspend fun insertUser(user : User)

    /**
     * Elimina un usuario de la base de datos
     * @param user el usuario a eliminar
     */
    @Delete
    suspend fun deleteUser(user: User)
}