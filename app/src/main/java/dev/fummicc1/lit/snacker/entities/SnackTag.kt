package dev.fummicc1.lit.snacker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "snack_tag",
    foreignKeys = arrayOf(ForeignKey(
        entity = Snack::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("snack_id"),
        onDelete = ForeignKey.CASCADE
    ))
)
data class SnackTag(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "snack_id")
    val snackId: Int,
    @ColumnInfo(name = "tag_id")
    val tagId: Int
)
