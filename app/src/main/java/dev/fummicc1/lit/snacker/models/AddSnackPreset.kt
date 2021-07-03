package dev.fummicc1.lit.snacker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddSnackPreset(
    val url: String,
    val title: String?,
    val priority: Int
): Parcelable