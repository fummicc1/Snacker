package dev.fummicc1.lit.snacker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "snack_history")
data class SnackHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var url: String,
    @ColumnInfo(name = "thumbnail_url")
    var thumbnailUrl: String?,
    var title: String,
    @ColumnInfo(name = "viewed_at")
    var viewedAt: Date
)