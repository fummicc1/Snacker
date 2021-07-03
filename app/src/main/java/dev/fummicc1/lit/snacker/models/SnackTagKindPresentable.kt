package dev.fummicc1.lit.snacker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// Convert from SnackTagKind
@Parcelize
data class SnackTagKindPresentable(
    override val id: Int,
    var name: String,
    var isActive: Boolean
): Identifiable, Parcelable