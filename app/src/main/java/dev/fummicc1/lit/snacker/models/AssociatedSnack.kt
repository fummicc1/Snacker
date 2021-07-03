package dev.fummicc1.lit.snacker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AssociatedSnack(
    override val id: Int,
    val url: String,
    val thumbnailUrl: String?,
    val title: String,
    val priority: Int
): Identifiable, Parcelable