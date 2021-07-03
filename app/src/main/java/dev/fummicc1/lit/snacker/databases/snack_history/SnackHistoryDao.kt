package dev.fummicc1.lit.snacker.databases.snack_history

import androidx.room.*
import dev.fummicc1.lit.snacker.entities.SnackHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SnackHistoryDao {
    @Query("SELECT * FROM snack_history ORDER BY viewed_at DESC")
    fun observeAll(): Flow<List<SnackHistory>>
    @Query("SELECT * FROM snack_history ORDER BY viewed_at DESC")
    fun getAll(): List<SnackHistory>

    @Query("SELECT * FROM snack_history ORDER BY viewed_at DESC LIMIT :limit")
    fun observe(limit: Int): Flow<List<SnackHistory>>
    @Query("SELECT * FROM snack_history ORDER BY viewed_at DESC LIMIT :limit")
    fun get(limit: Int): List<SnackHistory>

    @Query("SELECT * FROM snack_history WHERE url == :url")
    fun filterWithUrl(url: String): SnackHistory?

    @Query("SELECT * FROM snack_history WHERE id == :id")
    fun getWithId(id: Int): SnackHistory?

    @Insert
    fun create(snackHistory: SnackHistory): Long

    @Update
    fun update(snackHistory: SnackHistory)

    @Delete
    fun delete(snackHistory: SnackHistory)
}