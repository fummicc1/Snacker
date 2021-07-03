package dev.fummicc1.lit.snacker.databases

import androidx.room.TypeConverter
import dev.fummicc1.lit.snacker.entities.Snack
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let {
        Date(it)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? = date?.time?.toLong()

    @TypeConverter
    fun bookmarkStatusToString(bookmarkStatus: Snack.BookmarkStatus?): String? = bookmarkStatus?.name

    @TypeConverter
    fun fromString(value: String?): Snack.BookmarkStatus? = value?.let {
        when (it) {
            Snack.BookmarkStatus.SAVED.name -> Snack.BookmarkStatus.SAVED
            Snack.BookmarkStatus.ARCHIVED.name -> Snack.BookmarkStatus.ARCHIVED
            else -> null
        }
    }
}