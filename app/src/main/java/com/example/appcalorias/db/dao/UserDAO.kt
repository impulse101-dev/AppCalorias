package com.example.appcalorias.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
//import androidx.room.Transaction
import com.example.appcalorias.db.model.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM User")
    suspend fun getAllUsers() : List<User>

    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun getUserById(userId: Int) : User?

    @Query("SELECT * FROM User ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser() : User?

//    @Transaction
//    @Query("SELECT * FROM User WHERE id = :userId")
//    suspend fun getUserWithRecords(userId: Int) : UserWithRecords


    @Insert
    suspend fun insertUser(user : User)

    @Delete
    /**
     * Este metodo debe de usarse con la lista de usuarios que se saca de la base de datos
     * @param users lista de usuarios a eliminar
     * @see getAllUsers
     */
    suspend fun deleteAll(users : List<User>)

    @Delete
    /**
     * Elimina un usuario de la base de datos
     * @param user el usuario a eliminar
     */
    suspend fun deleteUser(user: User)
}