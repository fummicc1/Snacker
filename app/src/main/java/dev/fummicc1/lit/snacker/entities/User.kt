package dev.fummicc1.lit.snacker.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val interestFields: List<String>
)