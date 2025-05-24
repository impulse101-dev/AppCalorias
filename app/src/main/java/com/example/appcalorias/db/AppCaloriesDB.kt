package com.example.appcalorias.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcalorias.db.dao.UserDAO
import com.example.appcalorias.db.model.User


@Database(
    entities = [User::class],
    version = 5
)
abstract class AppCaloriesDB : RoomDatabase() {
    abstract fun getUserDAO() : UserDAO
}