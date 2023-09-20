package com.paruyr.scopictask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    val commonViewModelScope: CoroutineScope = viewModelScope
    open fun onClear() {
        commonViewModelScope.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        onClear()
    }
}