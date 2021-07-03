package dev.fummicc1.lit.snacker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snack_tag_kind")
data class SnackTagKind(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    @ColumnInfo(name = "is_active")
    var isActive: Boolean
)
