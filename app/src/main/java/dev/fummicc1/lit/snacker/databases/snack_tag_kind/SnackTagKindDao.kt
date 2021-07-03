package dev.fummicc1.lit.snacker.databases.snack_tag_kind

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.fummicc1.lit.snacker.entities.SnackTagKind
import kotlinx.coroutines.flow.Flow

@Dao
interface SnackTagKindDao {

    @Query("SELECT * FROM snack_tag_kind")
    fun observeAll(): Flow<List<SnackTagKind>>

    @Query("SELECT * FROM snack_tag_kind")
    fun getAll(): List<SnackTagKind>

    @Query("SELECT * FROM snack_tag_kind WHERE is_active == :isActive")
    fun observeFilteredWithActivation(isActive: Boolean): Flow<List<SnackTagKind>>

    @Query("SELECT * FROM snack_tag_kind WHERE is_active == :isActive LIMIT :limit")
    fun observeFilteredWithActivation(isActive: Boolean, limit: Int): Flow<List<SnackTagKind>>


    @Query("SELECT * FROM snack_tag_kind WHERE is_active == :isActive")
    fun filterWithActivation(isActive: Boolean): List<SnackTagKind>

    @Query("SELECT * FROM snack_tag_kind WHERE id == :id")
    fun getWithTagKindId(id: Long): SnackTagKind

    @Query("SELECT snack_tag_kind.id, snack_tag_kind.name, snack_tag_kind.is_active FROM snack_tag_kind INNER JOIN snack_tag ON snack_id == :snackId WHERE snack_tag.tag_id == snack_tag_kind.id")
    fun getSnackTags(snackId: Int): List<SnackTagKind>

    @Query("SELECT * FROM snack_tag_kind WHERE name == :name")
    fun searchWithName(name: String): SnackTagKind?

    @Insert
    fun create(snackTag: SnackTagKind): Long

    @Update
    fun update(snackTag: SnackTagKind)

    @Delete
    fun delete(snackTagKind: SnackTagKind)
}