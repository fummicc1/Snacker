package dev.fummicc1.lit.snacker.databases.stock_history

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.fummicc1.lit.snacker.entities.StockHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface StockHistoryDao {
    @Query("SELECT * FROM stock_history")
    fun observeAll(): Flow<List<StockHistory>>

    @Query("SELECT * FROM stock_history WHERE id == :id")
    fun fetch(id: Int): StockHistory?

    @Query("SELECT * FROM stock_history WHERE date == :dateSince1970")
    fun fetch(dateSince1970: Long): StockHistory?

    @Insert
    fun create(stockHistory: StockHistory)

    @Update
    fun update(stockHistory: StockHistory)

    @Delete
    fun delete(stockHistory: StockHistory)
}