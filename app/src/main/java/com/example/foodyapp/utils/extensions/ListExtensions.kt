package com.example.foodyapp.utils.extensions

fun <T> List<T>.getOrNull(index: Int, orDefault: T? = null): T? {
    return if (index in 0..lastIndex) get(index) else orDefault
}