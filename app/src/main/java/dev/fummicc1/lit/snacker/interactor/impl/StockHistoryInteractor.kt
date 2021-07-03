package dev.fummicc1.lit.snacker.interactor.impl

import dev.fummicc1.lit.snacker.MyApplication
import dev.fummicc1.lit.snacker.entities.StockHistory
import dev.fummicc1.lit.snacker.interactor.interfaces.StockHistoryUseCase
import dev.fummicc1.lit.snacker.models.StockHistoryPresentable
import dev.fummicc1.lit.snacker.repositories.impl.StockHistoryRepository
import dev.fummicc1.lit.snacker.repositories.interfaces.IStockHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class StockHistoryInteractor(application: MyApplication): StockHistoryUseCase {

    private val stockHistoryRepository: IStockHistoryRepository = StockHistoryRepository(application.snackDatabase)

    override fun observeAll(): Flow<List<StockHistoryPresentable>> {
        return stockHistoryRepository.observeAll().map {
            it.map {
                StockHistoryPresentable(
                    it.id,
                    it.date,
                    it.count
                )
            }
        }
    }

    override fun searchWithCurrentTime(): StockHistoryPresentable? {
        var current = Date()
        val calendar = Calendar.getInstance()
        calendar.time = current
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        current = calendar.time
        return stockHistoryRepository.searchWithTime(current.time)?.let {
            StockHistoryPresentable(
                it.id,
                it.date,
                it.count
            )
        }
    }

    override fun create(stockHistoryPresentable: StockHistoryPresentable) {
        return stockHistoryRepository.create(StockHistory(
            0,
            stockHistoryPresentable.date,
            stockHistoryPresentable.count
        ))
    }

    override fun update(stockHistoryPresentable: StockHistoryPresentable) {
        val history = stockHistoryRepository.fetch(stockHistoryPresentable.id)?.apply {
            date = stockHistoryPresentable.date
            count = stockHistoryPresentable.count
        }
        if (history == null) return
        stockHistoryRepository.update(history)
    }

    override fun delete(stockHistoryPresentable: StockHistoryPresentable) {
        val history = stockHistoryRepository.fetch(stockHistoryPresentable.id)
        if (history == null) return
        stockHistoryRepository.delete(history)
    }

}