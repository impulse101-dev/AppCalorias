package com.example.appcalorias.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appcalorias.db.model.Record

@Dao
interface RecordDAO {

    @Query("SELECT * FROM Record")
    suspend fun getAllRecords() : List<Record>

    @Query("SELECT * FROM Record r INNER JOIN User u ON r.idUser = u.id WHERE u.id = :userId")
    suspend fun getRecordsByUserId(userId: Int): List<Record>

    @Insert
    suspend fun insertRecord(record: Record)
}