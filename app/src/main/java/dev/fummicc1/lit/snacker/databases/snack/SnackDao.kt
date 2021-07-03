package dev.fummicc1.lit.snacker.databases.snack

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.entities.SnackAndTags
import dev.fummicc1.lit.snacker.entities.SnackTag
import dev.fummicc1.lit.snacker.entities.SnackTagKind
import kotlinx.coroutines.flow.Flow

@Dao
interface SnackDao {

    @Transaction
    @Query("SELECT * FROM snack")
    fun observeAll(): Flow<List<SnackAndTags>>

    @Query("SELECT * FROM snack")
    fun getAll(): List<Snack>

    @Query("SELECT * FROM snack WHERE status == (:status) ORDER BY priority DESC LIMIT :limit")
    fun getAllSortWithPriorityAndFilterWithStatus(status: Snack.BookmarkStatus, limit: Int): List<SnackAndTags>

    @Query("SELECT * FROM snack WHERE status == (:status) ORDER BY priority DESC")
    fun getAllSortWithPriorityAndFilterWithStatus(status: Snack.BookmarkStatus): List<SnackAndTags>

    @Query("SELECT * FROM snack WHERE status == (:status) ORDER BY priority DESC")
    fun observeAllSortWithPriorityAndFilterWithStatus(status: Snack.BookmarkStatus): Flow<List<SnackAndTags>>

    @Query("SELECT * FROM snack WHERE status == (:status) ORDER BY priority DESC LIMIT :limit")
    fun observeAllSortWithPriorityAndFilterWithStatus(status: Snack.BookmarkStatus, limit: Int): Flow<List<SnackAndTags>>

    @Query("SELECT * FROM snack ORDER BY priority DESC")
    fun observeAllSortWithPriority(): Flow<List<SnackAndTags>>

    @Query("SELECT * FROM snack WHERE status == :status")
    fun observeAndFilterWithStatus(status: Snack.BookmarkStatus): Flow<List<SnackAndTags>>

    @Query("SELECT * FROM snack WHERE status == :status ORDER BY priority DESC")
    fun observeAndFilterWithStatusSortWithPriority(status: Snack.BookmarkStatus): Flow<List<SnackAndTags>>

    @Query("SELECT * FROM snack WHERE title LIKE '%' || :title || '%'")
    fun filterWithTitle(title: String): List<SnackAndTags>

    @Query("SELECT snack.id, snack.url, snack.thumbnail_url, snack.title, snack.priority, snack.created_at, snack.status FROM snack INNER JOIN snack_tag ON snack_tag.snack_id == snack.id INNER JOIN snack_tag_kind ON snack_tag_kind.id == snack_tag.tag_id WHERE snack_tag_kind.name IN (:kinds) AND snack.status == (:status)")
    fun fetchSnackListAndFilterWithSnackKindNameListAndStatus(kinds: List<String>, status: Snack.BookmarkStatus): List<Snack>

    @Query("SELECT * FROM snack WHERE id == :id")
    fun observe(id: Int): Flow<SnackAndTags?>

    @Query("SELECT * FROM snack WHERE id == :id")
    fun fetchWithId(id: Int): SnackAndTags?

    // Note: need to enclose with `%` in query parameter.
    @Query("SELECT * FROM snack WHERE snack.title LIKE :query")
    fun search(query: String): List<SnackAndTags>

    @Query("SElECT * FROM snack ORDER BY created_at LIMIT :limit")
    fun getRecentlyAddedSnacks(limit: Int): List<SnackAndTags>

    @Query("SELECT * FROM snack ORDER BY created_at DESC LIMIT :limit")
    fun getOldSnacks(limit: Int): List<SnackAndTags>

    @Query("SELECT * FROM snack ORDER BY priority LIMIT :limit")
    fun getHighPrioritySnacks(limit: Int): List<SnackAndTags>

    @Query("SELECT * FROM snack WHERE title LIKE :titleQuery AND url != :url ORDER BY created_at DESC")
    fun getAssociatedSnacksFromTitle(titleQuery: String, url: String): List<SnackAndTags>

    @Query("SELECT * FROM snack WHERE url == :url")
    fun searchWithUrl(url: String): List<SnackAndTags>

    @Insert
    fun create(snack: Snack): Long

    @Update
    fun update(snack: Snack)

    @Delete
    fun delete(snack: Snack)
}