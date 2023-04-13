package com.example.mycontacts.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel : ViewModel() {
    protected var viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}