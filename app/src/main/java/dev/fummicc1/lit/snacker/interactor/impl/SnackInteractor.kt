package dev.fummicc1.lit.snacker.interactor.impl

import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.entities.SnackHistory
import dev.fummicc1.lit.snacker.entities.SnackTag
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackUseCase
import dev.fummicc1.lit.snacker.models.*
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackRepository
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackTagKindRepository
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackTagRepository
import dev.fummicc1.lit.snacker.repositories.impl.SnackHistoryRepository
import dev.fummicc1.lit.snacker.repositories.impl.SnackRepository
import dev.fummicc1.lit.snacker.repositories.impl.SnackTagKindRepository
import dev.fummicc1.lit.snacker.repositories.impl.SnackTagRepository
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class SnackInteractor(application: MyApplication) : SnackUseCase {

    private val snackRepository: ISnackRepository = SnackRepository(application.snackDatabase)
    private val snackTagRepository: ISnackTagRepository =
        SnackTagRepository(application.snackDatabase)
    private val snackTagKindRepository: ISnackTagKindRepository =
        SnackTagKindRepository(application.snackDatabase)
    private val snackHistoryRepository: ISnackHistoryRepository =
        SnackHistoryRepository(application.snackDatabase)

    override fun observeAllSnack(): Flow<List<SnackPresentable>> {
        return snackRepository.allSnackList.map {
            it.map {
                SnackPresentable(
                    it.id,
                    it.url,
                    it.thumbnailUrl,
                    it.title,
                    it.priority,
                    searchAssociatedSnackFromTitle(it.title, it.url),
                    fetchSnackTagPresentables(it.id),
                    it.status
                )
            }
        }
    }

    override fun observeFilteredWithStatus(
        status: Snack.BookmarkStatus,
        limit: Int?
    ): Flow<List<SnackPresentable>> {
        return snackRepository.observeAllSnackList(status, limit).map {
            it.map {
                SnackPresentable(
                    it.id,
                    it.url,
                    it.thumbnailUrl,
                    it.title,
                    it.priority,
                    searchAssociatedSnackFromTitle(it.title, it.url),
                    fetchSnackTagPresentables(it.id),
                    it.status
                )
            }
        }
    }

    override fun observeSnack(id: Int): Flow<SnackPresentable?> {
        return snackRepository.observeSnackWithId(id).map {
            if (it == null) return@map null
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }

    override fun fetchAllSnack(): List<SnackPresentable> {
        return snackRepository.getAllSnackList().map {
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }

    override fun filterWithTitle(title: String): List<SnackPresentable> {
        return snackRepository.filterWithTitle(title).map {
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }

    override fun fetchSnack(id: Int): SnackPresentable? {
        return snackRepository.fetchSnackWithId(id)?.let {
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }

    override fun searchSnackWithUrl(url: String): SnackPresentable? {
        return snackRepository.searchSnackWithUrl(url)?.let {
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }

    override fun filterhWithStatus(status: Snack.BookmarkStatus): List<SnackPresentable> {
        return snackRepository.getAllSnackList(status).map {
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }

    override fun createSnackAndTags(snack: SnackPresentable, tags: List<Int>) {
        val _snack = Snack(
            0,
            snack.url,
            snack.thumbnailUrl,
            snack.title,
            snack.priority,
            Date(),
            snack.status
        )
        val snackId = snackRepository.createSnack(_snack)
        val tags = tags.map {
            SnackTag(
                0,
                snackId.toInt(),
                it
            )
        }
        tags.forEach {
            snackTagRepository.createSnackTag(it)
        }
    }

    override fun updateSnack(id: Int, newSnack: SnackPresentable) {
        val snack = snackRepository.fetchSnackWithId(id)?.apply {
            url = newSnack.url
            thumbnailUrl = newSnack.thumbnailUrl
            title = newSnack.title
            priority = newSnack.priority
            status = newSnack.status
        }
        if (snack == null) return
        snackRepository.updateSnack(snack)
    }

    override fun deleteSnack(id: Int) {
        snackRepository.fetchSnackWithId(id)?.apply {
            snackRepository.deleteSnack(this)
        }
    }

    override fun fetchSnackTagPresentables(snackId: Int): List<SnackTagPresentable> {
        return snackTagRepository.fetchSnackTags(snackId).map {
            val snackTagKind = snackTagKindRepository.getWithSnackTagKindId(it.tagId)
            SnackTagPresentable(
                it.id,
                snackTagKind.name
            )
        }
    }

    override fun updateSnackTagPresentables(
        snack: SnackPresentable,
        snackTagKindPresentables: List<SnackTagKindPresentable>
    ) {
        val shouldDeleteList: MutableList<SnackTagPresentable> = mutableListOf()
        val shouldAddList: MutableList<SnackTagKindPresentable> = mutableListOf()

        for (currentTag in snack.tags) {
            val shouldDelete = snackTagKindPresentables.map { it.name }.contains(currentTag.name).not()

            if (shouldDelete) {
                shouldDeleteList.add(currentTag)
            }
        }

        for (newTag in snackTagKindPresentables) {
            val shouldAdd = snack.tags.map { it.name }.contains(newTag.name).not()

            if (shouldAdd) {
                shouldAddList.add(newTag)
            }
        }

        shouldDeleteList
            .map {
                val snackTagKind = snackTagKindRepository.searchWithName(it.name)
                if (snackTagKind == null) {
                    return@map null
                }
                return@map SnackTag(
                    it.id,
                    snack.id,
                    snackTagKind.id
                )
            }
            .filterNotNull()
            .forEach {
                snackTagRepository.deleteSnackTag(it)
            }

        shouldAddList
            .map {
                return@map SnackTag(
                    0,
                    snack.id,
                    it.id
                )
            }
            .forEach {
                snackTagRepository.createSnackTag(it)
            }
    }

    override fun searchAssociatedSnackFromTitle(title: String, url: String): List<AssociatedSnack> {
        // スペースで分割
        val words = title.split(" ").filter { it.length > 1 }
        val result: MutableList<AssociatedSnack> = mutableListOf()
        words.forEach {
            snackRepository.getAssociatedSnacksFromTitle("%${it}%", url).map { snack ->
                AssociatedSnack(
                    snack.id,
                    snack.url,
                    snack.thumbnailUrl,
                    snack.title,
                    snack.priority,
                )
            }.apply {
                result.addAll(this)
            }
        }
        return result.toSet().toList() // Remove the duplicated.
    }

    override fun convertAssociatedToSnack(associatedSnack: AssociatedSnack): SnackPresentable {
        val snack = snackRepository.fetchSnackWithId(associatedSnack.id)
        if (snack == null) {
            return associatedSnack.let {
                SnackPresentable(
                    0,
                    it.url,
                    it.thumbnailUrl,
                    it.title,
                    it.priority,
                    searchAssociatedSnackFromTitle(it.title, it.url),
                    listOf(),
                    null
                )
            }
        }
        return snack.let {
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }

    override fun observeRecentlySancks(limit: Int?): Flow<List<RecentlyViewedSnackPresentable>> {
        return snackHistoryRepository.observeAll(limit).map {
            it.map {
                RecentlyViewedSnackPresentable(
                    it.id,
                    it.url,
                    it.thumbnailUrl,
                    it.title,
                    it.viewedAt
                )
            }
        }
    }

    override fun observeSnackTagPresentable(snackId: Int): Flow<List<SnackTagPresentable>> {
        return snackTagRepository.observeSnackTags(snackId).map {
            it.map {
                val snackTagKind = snackTagKindRepository.getWithSnackTagKindId(it.tagId)
                SnackTagPresentable(
                    it.id,
                    snackTagKind.name
                )
            }
        }
    }

    override fun getRecentlySancks(limit: Int?): List<RecentlyViewedSnackPresentable> {
        return snackHistoryRepository.getAll(limit).map {
            RecentlyViewedSnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.viewedAt
            )
        }
    }

    override fun fetchSameUrlSnackHistory(url: String): RecentlyViewedSnackPresentable? {
        return snackHistoryRepository.filterWithUrl(url)?.let {
            RecentlyViewedSnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.viewedAt
            )
        }
    }

    override fun fetchHistory(id: Int): RecentlyViewedSnackPresentable? {
        return snackHistoryRepository.get(id)?.let {
            RecentlyViewedSnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.viewedAt
            )
        }
    }

    override fun createRecentlyViewedSnack(recentlyViewedSnackPresentable: RecentlyViewedSnackPresentable) {
        val snackHistory = SnackHistory(
            0,
            recentlyViewedSnackPresentable.url,
            recentlyViewedSnackPresentable.thumbnailUrl,
            recentlyViewedSnackPresentable.title,
            recentlyViewedSnackPresentable.viewedAt
        )
        snackHistoryRepository.create(snackHistory)
    }

    override fun updateRecentlyViewedSnack(recentlyViewedSnackPresentable: RecentlyViewedSnackPresentable) {
        snackHistoryRepository.get(recentlyViewedSnackPresentable.id)?.apply {
            url = recentlyViewedSnackPresentable.url
            thumbnailUrl = recentlyViewedSnackPresentable.thumbnailUrl
            title = recentlyViewedSnackPresentable.title
            viewedAt = recentlyViewedSnackPresentable.viewedAt
            snackHistoryRepository.update(this)
        }
    }

    override fun convertHistoryToSnack(recentlyViewedSnackPresentable: RecentlyViewedSnackPresentable): SnackPresentable {
        val existedSanck = snackRepository.searchSnackWithUrl(recentlyViewedSnackPresentable.url)
        if (existedSanck == null) {
            return recentlyViewedSnackPresentable.let {
                SnackPresentable(
                    0,
                    it.url,
                    it.thumbnailUrl,
                    it.title,
                    0,
                    searchAssociatedSnackFromTitle(it.title, it.url),
                    listOf(),
                    null
                )
            }
        }
        return existedSanck.let {
            SnackPresentable(
                it.id,
                it.url,
                it.thumbnailUrl,
                it.title,
                it.priority,
                searchAssociatedSnackFromTitle(it.title, it.url),
                fetchSnackTagPresentables(it.id),
                it.status
            )
        }
    }
}