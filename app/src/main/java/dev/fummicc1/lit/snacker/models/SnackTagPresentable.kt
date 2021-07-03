package dev.fummicc1.lit.snacker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SnackTagPresentable(
    override val id: Int,
    val name: String
): Identifiable, Parcelable