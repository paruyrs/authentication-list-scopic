package com.paruyr.scopictask.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paruyr.scopictask.data.db.dao.UserItemDao
import com.paruyr.scopictask.data.db.entity.UserItemEntity

@Database(entities = [UserItemEntity::class], version = 1, exportSchema = false)
abstract class UserItemDatabase : RoomDatabase() {
    abstract fun userItemDao(): UserItemDao
}