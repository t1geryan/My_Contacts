package com.example.mycontacts.presenter.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

fun Fragment.fragmentResult() = requireActivity() as FragmentResultApi

interface FragmentResultApi {
    fun <T : Parcelable> publishResult(requestKey: String, result: T)

    fun <T : Parcelable> listenResult(requestKey : String, clazz: Class<T>, owner: LifecycleOwner, listener: (T) -> Unit)
}