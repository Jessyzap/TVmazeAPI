package com.api.tvmaze.utils

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

inline fun <reified T : Parcelable> Fragment.getParcelableArg(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        arguments?.getParcelable(key)
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}