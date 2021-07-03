package dev.fummicc1.lit.snacker.models

import java.util.*

data class StockHistoryPresentable(
    override val id: Int,
    val date: Date,
    var count: Int
): Identifiable