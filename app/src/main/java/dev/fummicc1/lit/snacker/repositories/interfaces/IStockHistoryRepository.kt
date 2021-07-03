package dev.fummicc1.lit.snacker.repositories.interfaces

import androidx.lifecycle.LiveData
import dev.fummicc1.lit.snacker.entities.StockHistory
import kotlinx.coroutines.flow.Flow

interface IStockHistoryRepository {
    fun observeAll(): Flow<List<StockHistory>>
    fun searchWithTime(time: Long): StockHistory?
    fun fetch(id: Int): StockHistory?
    fun update(stockHistory: StockHistory)
    fun create(stockHistory: StockHistory)
    fun delete(stockHistory: StockHistory)
}