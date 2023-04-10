package com.example.mycontacts.domain.model

sealed class Result<T>(open val data: T? = null) {
    class Success<T>(override val data: T) : Result<T>(data)

    class Error<T>(val message: String? = null) : Result<T>()

    class Loading<T> : Result<T>()

    class EmptyOrNull<T> : Result<T>()
}

