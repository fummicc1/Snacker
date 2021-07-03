package dev.fummicc1.lit.snacker.repositories.interfaces

import dev.fummicc1.lit.snacker.entities.SnackHistory
import kotlinx.coroutines.flow.Flow

interface ISnackHistoryRepository {
    fun observeAll(limit: Int?): Flow<List<SnackHistory>>
    fun getAll(limit: Int?): List<SnackHistory>

    fun filterWithUrl(url: String): SnackHistory?

    fun get(id: Int): SnackHistory?

    fun create(snackHistory: SnackHistory): Int
    fun update(snackHistory: SnackHistory)
    fun delete(snackHistory: SnackHistory)
}