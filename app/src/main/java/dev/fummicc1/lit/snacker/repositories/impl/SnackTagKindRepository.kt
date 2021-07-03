package dev.fummicc1.lit.snacker.repositories.impl

import android.util.Log
import dev.fummicc1.lit.snacker.databases.SnackDatabase
import dev.fummicc1.lit.snacker.entities.SnackTagKind
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackTagKindRepository
import kotlinx.coroutines.flow.Flow

class SnackTagKindRepository(val database: SnackDatabase) : ISnackTagKindRepository {
    override fun createSnackTagKind(snackTagKind: SnackTagKind): Int {
        return database.snackTagKindDao().create(snackTagKind).toInt()
    }

    override fun observeAllSnackTagKind(): Flow<List<SnackTagKind>> {
        return database.snackTagKindDao().observeAll()
    }

    override fun filterWithIsActive(isActive: Boolean): List<SnackTagKind> {
        return database.snackTagKindDao().filterWithActivation(isActive)
    }

    override fun getAllSnackTagKinds(): List<SnackTagKind> {
        return database.snackTagKindDao().getAll()
    }

    override fun observeFilteredWithActivation(
        isActive: Boolean,
        limit: Int?
    ): Flow<List<SnackTagKind>> {
        return if (limit == null) database.snackTagKindDao().observeFilteredWithActivation(isActive)
        else database.snackTagKindDao().observeFilteredWithActivation(isActive, limit)
    }

    override fun getSnackTagKindWithSnackId(snackId: Int): List<SnackTagKind> {
        return database.snackTagKindDao().getSnackTags(snackId).apply {
            Log.d("SnackTagKindRepository", this.toString())
        }
    }

    override fun getWithSnackTagKindId(id: Int): SnackTagKind {
        return database.snackTagKindDao().getWithTagKindId(id.toLong())
    }

    override fun update(snackTagKind: SnackTagKind) {
        database.snackTagKindDao().update(snackTagKind)
    }

    override fun delete(snackTagKind: SnackTagKind) {
        database.snackTagKindDao().delete(snackTagKind)
    }

    override fun searchWithName(name: String): SnackTagKind? {
        return database.snackTagKindDao().searchWithName(name)
    }
}