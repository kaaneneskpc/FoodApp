package com.example.foodyapp.utils.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView.setSpannable(
    source: String,
    vararg spannableTexts: Pair<String, (() -> Unit)>,
    isUnderlineText: Boolean = false,
    spanColor: Int? = null,
    spanTypeface: Typeface? = null,
    highlightColor: Int = Color.TRANSPARENT,
) {
    if (spannableTexts.isEmpty())
        throw IllegalArgumentException("spannableTexts cannot be null!")

    if (source.isBlank())
        throw IllegalArgumentException("source cannot be null!")

    val spannableString = SpannableString(source)

    spannableTexts.forEach { spannableText ->
        val (spanText, onClick) = spannableText

        if (spanText.isEmpty())
            throw IllegalArgumentException("spanText cannot be null!")

        val (textStart, textEnd) = source.getTextPosition(spanText)

        spannableString.setSpan(

            object: ClickableSpan() {
                override fun onClick(p0: View) {
                    onClick.invoke()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = isUnderlineText
                    spanColor?.let {
                        ds.color = it
                    }
                    spanTypeface?.let {
                        ds.typeface = it
                    }
                }
            },
            textStart,
            textEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    this.setText(spannableString, TextView.BufferType.SPANNABLE)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = highlightColor
}

fun String.getTextPosition(text: String): Pair<Int, Int> {
    return Pair(
        this.indexOf(text),
        this.indexOf(text) + text.length
    )
}