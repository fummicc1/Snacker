package dev.fummicc1.lit.snacker.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.fummicc1.lit.snacker.models.SnackPresentable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
data class Snack (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var url: String,
    @ColumnInfo(name = "thumbnail_url")
    var thumbnailUrl: String?,
    var title: String,
    var priority: Int,
    @ColumnInfo(name = "created_at")
    var createdAt: Date,
    var status: BookmarkStatus?
) {
    @Parcelize
    enum class BookmarkStatus: Parcelable {
        SAVED,
        ARCHIVED
    }
}