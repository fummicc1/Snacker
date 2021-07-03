package dev.fummicc1.lit.snacker.repositories.impl

import dev.fummicc1.lit.snacker.databases.SnackDatabase
import dev.fummicc1.lit.snacker.entities.Snack
import dev.fummicc1.lit.snacker.models.SnackPresentable
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SnackRepository(val database: SnackDatabase) : ISnackRepository {

    override val savedSnackList: Flow<List<Snack>> =
        database.snackDao().observeAndFilterWithStatusSortWithPriority(Snack.BookmarkStatus.SAVED).map {
            it.map {
                it.snack
            }
        }

    override val archivedSnackList: Flow<List<Snack>> =
        database.snackDao()
            .observeAndFilterWithStatusSortWithPriority(Snack.BookmarkStatus.ARCHIVED).map {
                it.map {
                    it.snack
                }
            }

    override val allSnackList: Flow<List<Snack>> =
        database.snackDao().observeAllSortWithPriority().map {
            it.map {
                it.snack
            }
        }

    override fun getAllSnackList(): List<Snack> {
        return database.snackDao().getAll()
    }

    override fun getAllSnackList(status: Snack.BookmarkStatus): List<Snack> {
        return database.snackDao().getAllSortWithPriorityAndFilterWithStatus(status).map {
            it.snack
        }
    }

    override fun observeAllSnackList(status: Snack.BookmarkStatus, limit: Int?): Flow<List<Snack>> {
        return if (limit == null) database.snackDao()
            .observeAllSortWithPriorityAndFilterWithStatus(status).map {
                it.map {
                    it.snack
                }
            }
        else database.snackDao().observeAllSortWithPriorityAndFilterWithStatus(status, limit).map {
            it.map {
                it.snack
            }
        }
    }

    override fun filterWithTitle(title: String): List<Snack> {
        return database.snackDao().filterWithTitle(title).map {
            it.snack
        }
    }

    override fun fetchSnackWithId(id: Int): Snack? {
        return database.snackDao().fetchWithId(id)?.snack
    }

    override fun observeSnackWithId(id: Int): Flow<Snack?> {
        return database.snackDao().observe(id).map {
            it?.snack
        }
    }

    override fun createSnack(snack: Snack): Long {
        return database.snackDao().create(snack)
    }

    override fun updateSnack(snack: Snack) {
        database.snackDao().update(snack)
    }

    override fun deleteSnack(snack: Snack) {
        database.snackDao().delete(snack)
    }

    override fun getAssociatedSnacksFromTitle(query: String, currentUrl: String): List<Snack> {
        return database.snackDao().getAssociatedSnacksFromTitle(query, currentUrl).map {
            it.snack
        }
    }

    override fun searchSnackWithUrl(url: String): Snack? {
        return database.snackDao().searchWithUrl(url).map {
            it.snack
        }.firstOrNull()
    }

    override fun fetchSavedSanckListFilteringWithTag(tagList: List<String>): List<Snack> {
        return database.snackDao().fetchSnackListAndFilterWithSnackKindNameListAndStatus(
            tagList,
            Snack.BookmarkStatus.SAVED
        )
    }

    override fun fetchArchivedSanckListFilteringWithTag(tagList: List<String>): List<Snack> {
        return database.snackDao().fetchSnackListAndFilterWithSnackKindNameListAndStatus(
            tagList,
            Snack.BookmarkStatus.ARCHIVED
        )
    }
}