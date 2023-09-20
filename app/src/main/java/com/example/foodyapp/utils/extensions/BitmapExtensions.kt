package com.example.foodyapp.utils.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun String.convertBase64ToByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}

fun ByteArray.convertByteArrayToBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}