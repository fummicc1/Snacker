package dev.fummicc1.lit.snacker.databases.snack_tag

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.fummicc1.lit.snacker.entities.SnackTag
import dev.fummicc1.lit.snacker.entities.SnackTagKind
import kotlinx.coroutines.flow.Flow

@Dao
interface SnackTagDao {

    @Query("SELECT * FROM snack_tag WHERE tag_id == :id")
    fun getWithSnackKindId(id: Long): List<SnackTag>

    @Query("SELECT * FROM snack_tag WHERE snack_id == :id")
    fun getWithSnackId(id: Long): List<SnackTag>

    @Query("SELECT * FROM snack_tag WHERE snack_id == :id")
    fun observeWithSnackId(id: Long): Flow<List<SnackTag>>

    @Insert
    fun createSnackTag(snackTag: SnackTag)

    @Delete
    fun deleteSnackTag(snackTag: SnackTag)
}