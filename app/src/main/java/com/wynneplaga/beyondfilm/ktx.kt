package com.wynneplaga.beyondfilm

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun Float.spToPx(displayMetrics: DisplayMetrics): Float =
    (this * displayMetrics.scaledDensity)
fun Float.dpToPx(displayMetrics: DisplayMetrics): Float =
    (this * displayMetrics.density)

fun View.onFirstLayout(block: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            block()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    var observer: Observer<T>? = null
    observer = Observer<T> {
        block(it)
        removeObserver(observer!!)
    }
    observe(lifecycleOwner, observer)
}