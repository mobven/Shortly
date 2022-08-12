package com.mobven.shortly.utils

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView

fun TextView.underLineText(text: String?){
    text?.let {
        val spannableString = SpannableString(text).apply {
            setSpan(UnderlineSpan(), 0, text.length, 0)
        }

        setText(spannableString)
    }
}