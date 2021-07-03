package dev.fummicc1.lit.snacker.repositories.interfaces

import dev.fummicc1.lit.snacker.entities.SnackTagKind
import kotlinx.coroutines.flow.Flow

interface ISnackTagKindRepository {
    fun observeFilteredWithActivation(isActive: Boolean, limit: Int?): Flow<List<SnackTagKind>>
    fun filterWithIsActive(isActive: Boolean): List<SnackTagKind>
    fun observeAllSnackTagKind(): Flow<List<SnackTagKind>>
    fun createSnackTagKind(snackTagKind: SnackTagKind): Int
    fun getAllSnackTagKinds(): List<SnackTagKind>
    fun getSnackTagKindWithSnackId(snackId: Int): List<SnackTagKind>
    fun getWithSnackTagKindId(id: Int): SnackTagKind
    fun searchWithName(name: String): SnackTagKind?
    fun update(snackTagKind: SnackTagKind)
    fun delete(snackTagKind: SnackTagKind)
}