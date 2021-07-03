package dev.fummicc1.lit.snacker.interactor.interfaces

import dev.fummicc1.lit.snacker.models.StockHistoryPresentable
import kotlinx.coroutines.flow.Flow

interface StockHistoryUseCase {
    fun observeAll(): Flow<List<StockHistoryPresentable>>
    fun searchWithCurrentTime(): StockHistoryPresentable?

    fun create(stockHistoryPresentable: StockHistoryPresentable)
    fun update(stockHistoryPresentable: StockHistoryPresentable)
    fun delete(stockHistoryPresentable: StockHistoryPresentable)
}