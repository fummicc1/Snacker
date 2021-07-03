package dev.fummicc1.lit.snacker.repositories.impl

import dev.fummicc1.lit.snacker.databases.SnackDatabase
import dev.fummicc1.lit.snacker.entities.SnackHistory
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackHistoryRepository
import kotlinx.coroutines.flow.Flow

class SnackHistoryRepository(private val database: SnackDatabase): ISnackHistoryRepository {
    override fun observeAll(limit: Int?): Flow<List<SnackHistory>> {
        if (limit == null) return database.snackHistoryDao().observeAll()
        return database.snackHistoryDao().observe(limit)
    }

    override fun getAll(limit: Int?): List<SnackHistory> {
        if (limit == null) return database.snackHistoryDao().getAll()
        return database.snackHistoryDao().get(limit)
    }

    override fun filterWithUrl(url: String): SnackHistory? {
        return database.snackHistoryDao().filterWithUrl(url)
    }

    override fun get(id: Int): SnackHistory? {
        return database.snackHistoryDao().getWithId(id)
    }

    override fun create(snackHistory: SnackHistory): Int {
        return database.snackHistoryDao().create(snackHistory).toInt()
    }

    override fun update(snackHistory: SnackHistory) {
        return database.snackHistoryDao().update(snackHistory)
    }

    override fun delete(snackHistory: SnackHistory) {
        return database.snackHistoryDao().delete(snackHistory)
    }
}