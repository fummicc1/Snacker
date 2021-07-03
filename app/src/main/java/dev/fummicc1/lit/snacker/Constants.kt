package dev.fummicc1.lit.snacker

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat

object Constants {
    val initialWebUrl: String = "https://www.google.com/"

    fun primaryColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.brown)
    }
}