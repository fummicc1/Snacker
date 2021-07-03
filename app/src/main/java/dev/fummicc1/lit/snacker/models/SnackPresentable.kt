package dev.fummicc1.lit.snacker.models

import android.os.Parcelable
import dev.fummicc1.lit.snacker.entities.Snack
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SnackPresentable(
    override val id: Int,
    val url: String,
    val thumbnailUrl: String?,
    val title: String,
    val priority: Int,
    var associatedSnacks: List<AssociatedSnack>,
    var tags: List<SnackTagPresentable>,
    var status: Snack.BookmarkStatus?,
): Identifiable, Parcelable