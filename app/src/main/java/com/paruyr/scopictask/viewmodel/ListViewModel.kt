package com.paruyr.scopictask.viewmodel

import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paruyr.scopictask.data.model.list.ItemData
import com.paruyr.scopictask.data.network.Result
import com.paruyr.scopictask.data.repository.ConfigRepository
import com.paruyr.scopictask.data.repository.FirestoreItemDataRepository
import com.paruyr.scopictask.data.repository.RoomItemDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val roomItemDataRepository: RoomItemDataRepository,
    private val firestoreItemDataRepository: FirestoreItemDataRepository,
    private val configRepository: ConfigRepository
) : ViewModel() {
    private val _navigation = MutableSharedFlow<Navigation>()
    val navigation: SharedFlow<Navigation> = _navigation

    private lateinit var localItemsFlow: Flow<List<ItemData>>
    private val firestoreItemsFlow: MutableSharedFlow<List<ItemData>> = MutableSharedFlow()

    private val firestoreSwitchState = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val switchableItemsLiveData: Flow<List<ItemData>> = firestoreSwitchState
        .flatMapLatest { switchState ->
            if (switchState) {
                firestoreItemsFlow
            } else {
                localItemsFlow
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun setup() = viewModelScope.launch {
        getUserItemsFromDb()
        getUserItemsFromFirestore()
    }

    fun navigateToProfile() = viewModelScope.launch {
        _navigation.emit(Navigation.Profile)
    }

    fun addItem() = viewModelScope.launch {
        _navigation.emit(Navigation.AddListItem)
    }

    fun removeItem(removedItem: ItemData) = viewModelScope.launch {

        if (firestoreSwitchState.value)
            deleteUserItemFromFirestore(removedItem)
        else
            deleteUserItemFromDb(removedItem)
    }

    fun switchFirestoreState(state: Boolean) = viewModelScope.launch {
        firestoreSwitchState.value = state
        if (state) {
            getUserItemsFromFirestore()
        }
    }

    fun addItemToList(addedItem: String) {
        firestoreSwitchState.value.let { switchState ->
            if (switchState) {
                insertUserItemToFirestore(addedItem)
            } else {
                insertUserItemToDb(addedItem)
            }
        }
    }

    private fun insertUserItemToDb(userItem: String) = viewModelScope.launch {
        roomItemDataRepository.insertItem(
            item = userItem,
            userName = configRepository.getLoggedInUserEmail()
        )
    }

    private fun getUserItemsFromDb() = viewModelScope.launch {
        localItemsFlow =
            roomItemDataRepository.getItems(configRepository.getLoggedInUserEmail())
    }

    private fun deleteUserItemFromDb(userItem: ItemData) = viewModelScope.launch {
        roomItemDataRepository.deleteItem(userItem, configRepository.getLoggedInUserEmail())
    }

    private fun insertUserItemToFirestore(userItem: String) = viewModelScope.launch {
        when (val result = firestoreItemDataRepository.insertItem(
            item = userItem,
            userName = configRepository.getLoggedInUserEmail()
        )) {
            is Result.Success -> {
                // Handle success here
                // You can perform any necessary actions when the item is inserted
                Log.v(TAG, "Success: Item Added to firestore")
                getUserItemsFromFirestore()

            }

            is Result.Error -> {
                // Handle error here
                // You can display an error message or take other appropriate actions
                val errorMessage = result.exception.message ?: "Unknown error"
                Log.e(TAG, "Error: $errorMessage")
            }
        }
    }

    private fun getUserItemsFromFirestore() = viewModelScope.launch {
        firestoreItemsFlow.emit(firestoreItemDataRepository.loadItems(configRepository.getLoggedInUserEmail()))
    }

    private fun deleteUserItemFromFirestore(userItem: ItemData) = viewModelScope.launch {
        when (val result = firestoreItemDataRepository.deleteItem(
            userItem,
            configRepository.getLoggedInUserEmail()
        )) {
            is Result.Success -> {
                // Handle success here
                // You can perform any necessary actions when the item is inserted
                Log.v(TAG, "Success: Item deleted from firestore")

            }

            is Result.Error -> {
                // Handle error here
                // You can display an error message or take other appropriate actions
                val errorMessage = result.exception.message ?: "Unknown error"
                Log.e(TAG, "Error: $errorMessage")
            }
        }
    }

    sealed class Navigation {
        data object Profile : Navigation()
        data object AddListItem : Navigation()
    }
}