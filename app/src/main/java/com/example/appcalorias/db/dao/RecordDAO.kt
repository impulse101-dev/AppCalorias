package com.example.appcalorias.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appcalorias.db.model.record.Record

@Dao
interface RecordDAO {

    @Query("SELECT * FROM Record")
    suspend fun getAllRecords() : List<Record>

    @Insert
    suspend fun insertRecord(record: Record)
}