package dev.fummicc1.lit.snacker.repositories.interfaces

import dev.fummicc1.lit.snacker.entities.SnackTag
import kotlinx.coroutines.flow.Flow

interface ISnackTagRepository {
    fun createSnackTag(snackTag: SnackTag)
    fun observeSnackTags(snackId: Int): Flow<List<SnackTag>>
    fun fetchSnackTags(snackId: Int): List<SnackTag>
    fun deleteSnackTag(snackTag: SnackTag)
}