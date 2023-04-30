package com.example.mycontacts.presenter.ui_utils

import android.content.Context
import android.util.DisplayMetrics

object RecyclerViewUtility {
    fun calculateNoOfColumns(
        context: Context, columnWidthDp: Float
    ): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}