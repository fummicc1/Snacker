package dev.fummicc1.lit.snacker.entities

import androidx.room.Embedded
import androidx.room.Relation

class SnackAndTags {
    @Embedded
    lateinit var snack: Snack

    @Relation(parentColumn = "id", entityColumn = "snack_id")
    lateinit var tags: List<SnackTag>
}