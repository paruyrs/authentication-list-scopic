package com.paruyr.scopictask.data.network

import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import com.google.firebase.firestore.FirebaseFirestore
import com.paruyr.scopictask.utils.Constants.FIRESTORE_ENTRIES_NODE
import com.paruyr.scopictask.utils.Constants.FIRESTORE_ITEM_NODE
import com.paruyr.scopictask.utils.Constants.FIRESTORE_USERS_NODE
import kotlinx.coroutines.tasks.await

interface FirestoreDataSource {
    suspend fun getItems(userName: String): MutableMap<String, String>
    suspend fun insertItem(item: String, userName: String): Result<Unit>
    suspend fun deleteItem(id: String, userName: String): Result<Unit>
}

class FirestoreDataSourceImpl(private var firestore: FirebaseFirestore) : FirestoreDataSource {
    override suspend fun getItems(userName: String): MutableMap<String, String> {
        val userNodeRef = firestore.collection(FIRESTORE_USERS_NODE).document(userName)
        val itemList = mutableMapOf<String, String>()

        // Query the user's entries
        try {
            val querySnapshot = userNodeRef.collection(FIRESTORE_ENTRIES_NODE).get().await()

            for (document in querySnapshot.documents) {
                val item = document.getString(FIRESTORE_ITEM_NODE)
                item?.let {
                    val id = document.id
                    itemList[id] = it
                }
                Log.d(TAG, "${document.id} => $item")
            }
        } catch (e: Exception) {
            // Handle query failure
            Log.e(TAG, "Error: ${e.message}")
        }
        // itemList contains the list of items for the user
        return itemList
    }

    override suspend fun insertItem(item: String, userName: String): Result<Unit> {
        return try {
            // Add a new document with a generated ID
            // Reference to the user's node
            val userNodeRef = firestore.collection(FIRESTORE_USERS_NODE).document(userName)

            // Generate a new random ID for the text entry
            val randomId = userNodeRef.collection(FIRESTORE_ENTRIES_NODE).document().id
            val itemData = mapOf(FIRESTORE_ITEM_NODE to item)

            // Set the text in the user's node with the random ID
            userNodeRef.collection(FIRESTORE_ENTRIES_NODE).document(randomId).set(itemData).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteItem(id: String, userName: String): Result<Unit> {
        return try {
            // Reference to the user's node
            val userNodeRef = firestore.collection(FIRESTORE_USERS_NODE).document(userName)

            // Reference to the specific item's document using the provided ID
            val itemDocumentRef = userNodeRef.collection(FIRESTORE_ENTRIES_NODE).document(id)

            // Check if the document exists
            val itemDocument = itemDocumentRef.get().await()
            if (itemDocument.exists()) {
                // If the document exists, delete it
                itemDocumentRef.delete().await()
            } else {
                // Handle the case where the item with the provided ID doesn't exist
                return Result.Error(Exception("Item with ID $id not found"))
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}