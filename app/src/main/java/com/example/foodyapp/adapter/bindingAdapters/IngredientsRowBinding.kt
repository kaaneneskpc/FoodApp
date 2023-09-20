package com.example.foodyapp.adapter.bindingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

class IngredientsRowBinding {

    companion object {
        @BindingAdapter("setAmount")
        @JvmStatic
        fun setAmount(textView: TextView, amount: Double){
            textView.text = amount.toString()
        }
    }

}