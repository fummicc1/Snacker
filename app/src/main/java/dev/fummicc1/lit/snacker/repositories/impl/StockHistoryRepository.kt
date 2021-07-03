package dev.fummicc1.lit.snacker.repositories.impl

import androidx.lifecycle.LiveData
import dev.fummicc1.lit.snacker.databases.SnackDatabase
import dev.fummicc1.lit.snacker.entities.StockHistory
import dev.fummicc1.lit.snacker.repositories.interfaces.IStockHistoryRepository
import kotlinx.coroutines.flow.Flow

class StockHistoryRepository(val database: SnackDatabase): IStockHistoryRepository {
    override fun observeAll(): Flow<List<StockHistory>> {
        return database.stockHistoryDao().observeAll()
    }

    override fun searchWithTime(time: Long): StockHistory? {
        return database.stockHistoryDao().fetch(time)
    }

    override fun fetch(id: Int): StockHistory? {
        return database.stockHistoryDao().fetch(id)
    }

    override fun update(stockHistory: StockHistory) {
        database.stockHistoryDao().update(stockHistory)
    }

    override fun create(stockHistory: StockHistory) {
        database.stockHistoryDao().create(stockHistory)
    }

    override fun delete(stockHistory: StockHistory) {
        database.stockHistoryDao().delete(stockHistory)
    }
}