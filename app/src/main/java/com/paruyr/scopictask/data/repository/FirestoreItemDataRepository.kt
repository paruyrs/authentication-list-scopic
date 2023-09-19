package com.paruyr.scopictask.data.repository

import com.paruyr.scopictask.data.model.list.ItemData
import com.paruyr.scopictask.data.network.FirestoreDataSource
import com.paruyr.scopictask.data.network.Result

class FirestoreItemDataRepository(private val firestoreDataSource: FirestoreDataSource) {

    suspend fun loadItems(userName: String): List<ItemData> {
        val itemList = firestoreDataSource.getItems(userName)
        return wrapRawData(itemList)
    }

    suspend fun insertItem(item: String, userName: String): Result<Unit> {
        return firestoreDataSource.insertItem(item, userName)
    }

    suspend fun deleteItem(item: ItemData, userName: String): Result<Unit> {
        return firestoreDataSource.deleteItem(item.id, userName)
    }

    private fun wrapRawData(rawData: MutableMap<String, String>): List<ItemData> =
        if (rawData.isEmpty())
            emptyList()
        else
            rawData.map { (id, value) ->
                ItemData(id, value)
            }
}