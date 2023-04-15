package com.example.mycontacts.domain.model

sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()

    class Error<T>(val message: String? = null) : Result<T>()

    class Loading<T> : Result<T>()

    class EmptyOrNull<T> : Result<T>()
}

