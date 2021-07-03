package dev.fummicc1.lit.snacker.interactor.interfaces

import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.models.*
import kotlinx.coroutines.flow.Flow

interface SnackUseCase {

    // Snack
    fun observeAllSnack(): Flow<List<SnackPresentable>>
    fun observeFilteredWithStatus(status: Snack.BookmarkStatus, limit: Int?): Flow<List<SnackPresentable>>
    fun observeSnack(id: Int): Flow<SnackPresentable?>
    fun fetchAllSnack(): List<SnackPresentable>
    fun fetchSnack(id: Int): SnackPresentable?
    fun filterWithTitle(title: String): List<SnackPresentable>
    fun searchSnackWithUrl(url: String): SnackPresentable?
    fun filterhWithStatus(status: Snack.BookmarkStatus): List<SnackPresentable>
    fun createSnackAndTags(snack: SnackPresentable, tags: List<Int>)
    fun updateSnack(id: Int, newSnack: SnackPresentable)
    fun deleteSnack(id: Int)

    // SnackTag
    fun observeSnackTagPresentable(snackId: Int): Flow<List<SnackTagPresentable>>
    fun fetchSnackTagPresentables(snackId: Int): List<SnackTagPresentable>
    fun updateSnackTagPresentables(snack: SnackPresentable, snackTagKindPresentables: List<SnackTagKindPresentable>)

    // AssociatedSnack
    fun searchAssociatedSnackFromTitle(title: String, url: String): List<AssociatedSnack>
    fun convertAssociatedToSnack(associatedSnack: AssociatedSnack): SnackPresentable

    // RecentlySnackPresentable
    fun fetchHistory(id: Int): RecentlyViewedSnackPresentable?
    fun fetchSameUrlSnackHistory(url: String): RecentlyViewedSnackPresentable?
    fun getRecentlySancks(limit: Int?): List<RecentlyViewedSnackPresentable>
    fun observeRecentlySancks(limit: Int?): Flow<List<RecentlyViewedSnackPresentable>>
    fun createRecentlyViewedSnack(recentlyViewedSnackPresentable: RecentlyViewedSnackPresentable)
    fun updateRecentlyViewedSnack(recentlyViewedSnackPresentable: RecentlyViewedSnackPresentable)

    fun convertHistoryToSnack(recentlyViewedSnackPresentable: RecentlyViewedSnackPresentable): SnackPresentable
}