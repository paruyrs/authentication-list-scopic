package com.paruyr.scopictask.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paruyr.scopictask.data.db.entity.UserItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: UserItemEntity)

    @Delete
    suspend fun deleteItem(item: UserItemEntity)

    @Query("SELECT * FROM user_items WHERE user_name = :userName")
    fun getItemsByUserName(userName: String): Flow<List<UserItemEntity>>

}