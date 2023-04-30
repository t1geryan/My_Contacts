package com.example.mycontacts.presenter.state

sealed class UiState<T> {
    class Success<T>(val data: T) : UiState<T>()

    class Error<T>(val message: String? = null) : UiState<T>()

    class Loading<T> : UiState<T>()

    class EmptyOrNull<T> : UiState<T>()
}

