package dev.fummicc1.lit.snacker.interactor.interfaces

import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import kotlinx.coroutines.flow.Flow

interface SnackTagKindUseCase {

    fun observeAll(): Flow<List<SnackTagKindPresentable>>
    fun observeFilterWithActivation(isActive: Boolean, limit: Int?): Flow<List<SnackTagKindPresentable>>
    fun filterWithActivation(isActive: Boolean): List<SnackTagKindPresentable>
    fun getAll(): List<SnackTagKindPresentable>

    fun observe(id: Int): SnackTagKindPresentable
    fun get(id: Int): SnackTagKindPresentable
    fun searchWithName(name: String): SnackTagKindPresentable?

    fun create(name: String): Int
    fun update(snackTagKindPresentable: SnackTagKindPresentable)
    fun delete(tagKind: SnackTagKindPresentable)
}