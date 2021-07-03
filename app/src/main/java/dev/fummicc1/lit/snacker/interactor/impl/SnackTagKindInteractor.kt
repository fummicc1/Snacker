package dev.fummicc1.lit.snacker.interactor.impl

import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.entities.SnackTagKind
import dev.fummicc1.lit.snacker.interactor.interfaces.SnackTagKindUseCase
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackRepository
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackTagKindRepository
import dev.fummicc1.lit.snacker.repositories.interfaces.ISnackTagRepository
import dev.fummicc1.lit.snacker.models.SnackTagKindPresentable
import dev.fummicc1.lit.snacker.repositories.impl.SnackRepository
import dev.fummicc1.lit.snacker.repositories.impl.SnackTagKindRepository
import dev.fummicc1.lit.snacker.repositories.impl.SnackTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SnackTagKindInteractor(application: MyApplication) : SnackTagKindUseCase {
    private val snackRepository: ISnackRepository = SnackRepository(application.snackDatabase)
    private val snackTagRepository: ISnackTagRepository =
        SnackTagRepository(application.snackDatabase)
    private val snackTagKindRepository: ISnackTagKindRepository =
        SnackTagKindRepository(application.snackDatabase)

    override fun observeAll(): Flow<List<SnackTagKindPresentable>> {
        return snackTagKindRepository.observeAllSnackTagKind().map {
            it.map {
                SnackTagKindPresentable(
                    it.id,
                    it.name,
                    it.isActive
                )
            }
        }
    }

    override fun observeFilterWithActivation(isActive: Boolean, limit: Int?): Flow<List<SnackTagKindPresentable>> {
        return snackTagKindRepository.observeFilteredWithActivation(isActive, limit).map {
            it.map {
                SnackTagKindPresentable(
                    it.id,
                    it.name,
                    it.isActive
                )
            }
        }
    }

    override fun filterWithActivation(isActive: Boolean): List<SnackTagKindPresentable> {
        return snackTagKindRepository.filterWithIsActive(isActive).map {
            SnackTagKindPresentable(
                it.id,
                it.name,
                it.isActive
            )
        }
    }

    override fun getAll(): List<SnackTagKindPresentable> {
        return snackTagKindRepository.getAllSnackTagKinds().map {
            SnackTagKindPresentable(
                it.id,
                it.name,
                it.isActive
            )
        }
    }

    override fun observe(id: Int): SnackTagKindPresentable {
        throw NotImplementedError()
    }

    override fun get(id: Int): SnackTagKindPresentable {
        return snackTagKindRepository.getWithSnackTagKindId(id).let {
            SnackTagKindPresentable(
                it.id,
                it.name,
                it.isActive
            )
        }
    }

    override fun searchWithName(name: String): SnackTagKindPresentable? {
        return snackTagKindRepository.searchWithName(name)?.let {
            SnackTagKindPresentable(
                it.id,
                it.name,
                it.isActive
            )
        }
    }

    override fun create(name: String): Int {
        val snackTagKind = SnackTagKind(
            0,
            name,
            true
        )
        return snackTagKindRepository.createSnackTagKind(snackTagKind)
    }

    override fun update(snackTagKindPresentable: SnackTagKindPresentable) {
        val snackTag = snackTagKindRepository.getWithSnackTagKindId(snackTagKindPresentable.id).apply {
            name = snackTagKindPresentable.name
            isActive = snackTagKindPresentable.isActive
        }
        snackTagKindRepository.update(snackTag)
    }

    override fun delete(tagKind: SnackTagKindPresentable) {
        snackTagKindRepository.getWithSnackTagKindId(tagKind.id).let {
            snackTagKindRepository.delete(it)
        }
    }
}