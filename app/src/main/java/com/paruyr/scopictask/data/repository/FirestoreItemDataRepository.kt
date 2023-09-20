package com.paruyr.scopictask.data.repository

import com.paruyr.scopictask.data.model.list.ItemData
import com.paruyr.scopictask.data.network.FirestoreDataSource
import com.paruyr.scopictask.data.network.Result

interface FirestoreItemDataRepository {
    suspend fun loadItems(userName: String): List<ItemData>
    suspend fun insertItem(item: String, userName: String): Result<Unit>
    suspend fun deleteItem(item: ItemData, userName: String): Result<Unit>
}

class FirestoreItemDataRepositoryImpl(private val firestoreDataSource: FirestoreDataSource) :
    FirestoreItemDataRepository {

    override suspend fun loadItems(userName: String): List<ItemData> {
        val itemList = firestoreDataSource.getItems(userName)
        return wrapRawData(itemList)
    }

    override suspend fun insertItem(item: String, userName: String): Result<Unit> {
        return firestoreDataSource.insertItem(item, userName)
    }

    override suspend fun deleteItem(item: ItemData, userName: String): Result<Unit> {
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