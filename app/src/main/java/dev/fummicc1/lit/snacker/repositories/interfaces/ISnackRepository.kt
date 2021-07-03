package dev.fummicc1.lit.snacker.repositories.interfaces

import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.models.SnackPresentable
import kotlinx.coroutines.flow.Flow

interface ISnackRepository {
    val allSnackList: Flow<List<Snack>>
    val archivedSnackList: Flow<List<Snack>>
    val savedSnackList: Flow<List<Snack>>

    fun getAllSnackList(): List<Snack>
    fun getAllSnackList(status: Snack.BookmarkStatus): List<Snack>
    fun observeAllSnackList(status: Snack.BookmarkStatus, limit: Int?): Flow<List<Snack>>
    fun filterWithTitle(title: String): List<Snack>
    fun fetchSnackWithId(id: Int): Snack?
    fun observeSnackWithId(id: Int): Flow<Snack?>
    fun createSnack(snack: Snack): Long
    fun updateSnack(snack: Snack)
    fun deleteSnack(snack: Snack)
    fun getAssociatedSnacksFromTitle(query: String, currentUrl: String): List<Snack>
    fun searchSnackWithUrl(url: String): Snack?
    fun fetchSavedSanckListFilteringWithTag(tagList: List<String>): List<Snack>
    fun fetchArchivedSanckListFilteringWithTag(tagList: List<String>): List<Snack>
}