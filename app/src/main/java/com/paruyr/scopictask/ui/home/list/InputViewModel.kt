package com.paruyr.scopictask.ui.home.list

import com.paruyr.scopictask.utils.Constants.ITEM_TEXT_MAX_LENGTH
import com.paruyr.scopictask.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class InputViewModel : BaseViewModel() {
    private val _warningMessage = MutableSharedFlow<String?>()
    val warningMessage: SharedFlow<String?> = _warningMessage

    private val _input = MutableSharedFlow<String>()
    val input: SharedFlow<String> = _input

    fun updateCharCount(input: String) = commonViewModelScope.launch {
        if (input.length >= ITEM_TEXT_MAX_LENGTH) {
            _warningMessage.emit("Character limit reached!")
        } else {
            _warningMessage.emit(null)
        }
    }

    fun addItem(userInput: String) = commonViewModelScope.launch {
        if (userInput.isBlank()) {
            _warningMessage.emit("Invalid Data!")
        } else {
            _input.emit(userInput)
        }
    }
}