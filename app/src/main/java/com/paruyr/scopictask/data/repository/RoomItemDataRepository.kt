package com.paruyr.scopictask.data.repository

import com.paruyr.scopictask.data.db.RoomDbDataSource
import com.paruyr.scopictask.data.db.entity.UserItemEntity
import com.paruyr.scopictask.data.model.list.ItemData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomItemDataRepository(private val roomDataSource: RoomDbDataSource) {

    fun getItems(userName: String): Flow<List<ItemData>> {
        return roomDataSource.getItems(userName).map { userItems ->
            userItems.map {
                ItemData(
                    id = it.id.toString(),
                    value = it.item
                )
            }
        }
    }

    suspend fun insertItem(item: String, userName: String) {
        roomDataSource.insertItem(item, userName)
    }

    suspend fun deleteItem(item: ItemData, userName: String) {
        roomDataSource.deleteItem(
            UserItemEntity(
                id = item.id.toLong(),
                userName = userName,
                item = item.value
            )
        )
    }
}