package com.example.diceroller.data

import kotlinx.coroutines.flow.Flow

class DiceRollRepository(private val diceRollDao: DiceRollDao) {
    val allRolls: Flow<List<DiceRoll>> = diceRollDao.getAllRolls()

    suspend fun insert(diceRoll: DiceRoll) {
        diceRollDao.insertRoll(diceRoll)
    }

    suspend fun clearHistory() {
        diceRollDao.clearHistory()
    }
}
