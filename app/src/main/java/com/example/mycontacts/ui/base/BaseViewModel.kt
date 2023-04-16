package com.example.mycontacts.ui.base

import androidx.lifecycle.ViewModel
import com.example.mycontacts.ui.ui_utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected var viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    protected fun <T> fetchAsync(
        getData: Flow<T>,
        stateFlow: MutableStateFlow<UiState<T>>,
        isEmptyOrNull: (T) -> Boolean
    ) = viewModelScope.launch {
        stateFlow.value = UiState.Loading()
        try {
            getData.collect { data ->
                stateFlow.value = if (isEmptyOrNull(data)) {
                    UiState.EmptyOrNull()
                } else {
                    UiState.Success(data)
                }
            }
        } catch (e: Exception) {
            stateFlow.value = UiState.Error(e.message)
        }
    }
}