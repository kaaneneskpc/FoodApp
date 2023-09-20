package com.example.foodyapp.utils.extensions

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.reverseVisibility() {
    visibility = if (isVisible) View.GONE else View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.reverseInvisibility() {
    visibility = if (isInvisible) View.VISIBLE else View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}