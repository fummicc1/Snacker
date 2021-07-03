package dev.fummicc1.lit.snacker.repositories.impl

import dev.fummicc1.lit.snacker.databases.SnackDatabase
import dev.fummicc1.lit.snacker.entities.SnackTag
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackTagRepository
import kotlinx.coroutines.flow.Flow

class SnackTagRepository(val database: SnackDatabase): ISnackTagRepository {
    override fun createSnackTag(snackTag: SnackTag) {
        database.snackTagDao().createSnackTag(snackTag)
    }

    override fun deleteSnackTag(snackTag: SnackTag) {
        database.snackTagDao().deleteSnackTag(snackTag)
    }

    override fun observeSnackTags(snackId: Int): Flow<List<SnackTag>> {
        return database.snackTagDao().observeWithSnackId(snackId.toLong())
    }

    override fun fetchSnackTags(snackId: Int): List<SnackTag> {
        return database.snackTagDao().getWithSnackId(snackId.toLong())
    }
}