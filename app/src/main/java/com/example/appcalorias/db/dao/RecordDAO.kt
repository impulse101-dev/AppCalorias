package com.example.appcalorias.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.appcalorias.db.model.Record

/**
 * DAO para manejar las operaciones de la tabla Record en la base de datos.
 * Proporciona métodos para insertar, eliminar y consultar registros asociados a un usuario.
 * @author Adrian Salazar Escoriza
 */
@Dao
interface RecordDAO {

    /**
     * Obtiene una lista de registros asociados a un usuario específico.
     * @param userId El ID del usuario cuyos registros se desean obtener.
     * @return Una lista de registros ordenados por ID en orden descendente.
     * SELECT * FROM Record r INNER JOIN User u ON r.idUser = u.id WHERE u.id = :userId ORDER BY r.id DESC
     */
    @Query("SELECT * FROM Record r INNER JOIN User u ON r.idUser = u.id WHERE u.id = :userId ORDER BY r.id DESC")
    suspend fun getRecordsByUserId(userId: Int): List<Record>

    /**
     * Inserta un registro especifico en la base de datos.
     * @param record El registro a insertar.
     */
    @Insert
    suspend fun insertRecord(record: Record)

    /**
     * Elimina una lista de registros de la base de datos.
     * @param record Lista de registros a eliminar.
     */
    @Delete()
    suspend fun deleteRecords(record: List<Record>)

}