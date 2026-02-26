package com.example.diceroller.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dice_rolls")
data class DiceRoll(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val diceType: Int,
    val count: Int,
    val results: String,
    val sum: Int,
    val createdAt: Long = System.currentTimeMillis()
)
