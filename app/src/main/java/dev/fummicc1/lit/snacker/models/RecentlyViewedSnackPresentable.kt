package dev.fummicc1.lit.snacker.models

import androidx.room.ColumnInfo
import java.util.*

data class RecentlyViewedSnackPresentable(
    override val id: Int,
    var url: String,
    var thumbnailUrl: String?,
    var title: String,
    var viewedAt: Date
): Identifiable