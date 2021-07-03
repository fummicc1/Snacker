package dev.fummicc1.lit.snacker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "stock_history")
data class StockHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var date: Date,
    var count: Int,
)