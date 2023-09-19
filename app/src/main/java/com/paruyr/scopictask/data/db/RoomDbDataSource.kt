package com.paruyr.scopictask.data.db

import com.paruyr.scopictask.data.db.dao.UserItemDao
import com.paruyr.scopictask.data.db.entity.UserItemEntity
import kotlinx.coroutines.flow.Flow

class RoomDbDataSource(private val userItemDao: UserItemDao) {

    fun getItems(userName: String): Flow<List<UserItemEntity>> {
        return userItemDao.getItemsByUserName(userName)
    }

    suspend fun insertItem(item: String, userName: String) {
        userItemDao.insertItem(UserItemEntity(item = item, userName = userName))
    }

    suspend fun deleteItem(item: UserItemEntity) {
        userItemDao.deleteItem(item)
    }
}