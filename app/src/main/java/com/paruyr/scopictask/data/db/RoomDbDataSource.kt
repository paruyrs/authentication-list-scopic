package com.paruyr.scopictask.data.db

import com.paruyr.scopictask.data.db.dao.UserItemDao
import com.paruyr.scopictask.data.db.entity.UserItemEntity
import kotlinx.coroutines.flow.Flow

interface RoomDbDataSource {
    fun getItems(userName: String): Flow<List<UserItemEntity>>
    suspend fun insertItem(item: String, userName: String)
    suspend fun deleteItem(item: UserItemEntity)
}

class RoomDbDataSourceImpl(private val userItemDao: UserItemDao) : RoomDbDataSource {

    override fun getItems(userName: String): Flow<List<UserItemEntity>> {
        return userItemDao.getItemsByUserName(userName)
    }

    override suspend fun insertItem(item: String, userName: String) {
        userItemDao.insertItem(UserItemEntity(item = item, userName = userName))
    }

    override suspend fun deleteItem(item: UserItemEntity) {
        userItemDao.deleteItem(item)
    }
}