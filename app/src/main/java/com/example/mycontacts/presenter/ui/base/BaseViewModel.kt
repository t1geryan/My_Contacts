package com.example.mycontacts.presenter.ui.base

import androidx.lifecycle.ViewModel
import com.example.mycontacts.presenter.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected var viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    protected fun <T> collectAsUiState(
        dataFlow: Flow<T>,
        stateFlow: MutableStateFlow<UiState<T>>,
        checkEmptyDataBlock: (T) -> Boolean
    ) = viewModelScope.launch {
        stateFlow.value = UiState.Loading()
        try {
            dataFlow.collect { data ->
                stateFlow.value = if (checkEmptyDataBlock(data)) {
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