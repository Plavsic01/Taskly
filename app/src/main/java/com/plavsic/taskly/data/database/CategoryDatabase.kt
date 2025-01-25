package com.plavsic.taskly.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plavsic.taskly.data.database.dao.CategoryDAO
import com.plavsic.taskly.data.database.entities.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1)
abstract class CategoryDatabase:RoomDatabase() {
    abstract fun CategoryDAO() : CategoryDAO
}