package com.mobven.shortly.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.annotation.NavigationRes
import androidx.navigation.NavController
import androidx.navigation.NavGraph

fun TextView.underLineText(text: String?) {
    text?.let {
        val spannableString = SpannableString(text).apply {
            setSpan(UnderlineSpan(), 0, text.length, 0)
        }
        setText(spannableString)
    }
}

/**
 * Extension method to share for Context.
 */
fun Context.share(text: String, title: String): Boolean {
    val intent = Intent().apply {
        type = "text/plain"
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
    }
    return try {
        startActivity(Intent.createChooser(intent, title))
        true
    } catch (e: ActivityNotFoundException) {
        false
    }
}
