package com.example.appcalorias.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcalorias.db.dao.RecordDAO
import com.example.appcalorias.db.dao.UserDAO
import com.example.appcalorias.db.model.user.User
import com.example.appcalorias.db.model.record.Record


@Database(
    entities = [User::class, Record::class],
    version = 9
)
abstract class AppCaloriesDB : RoomDatabase() {
    abstract fun getUserDAO() : UserDAO

    abstract fun getRecordDAO() : RecordDAO
}