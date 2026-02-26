package com.example.diceroller.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiceRollDao {
    @Query("SELECT * FROM dice_rolls ORDER BY createdAt DESC")
    fun getAllRolls(): Flow<List<DiceRoll>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoll(diceRoll: DiceRoll)

    @Query("DELETE FROM dice_rolls")
    suspend fun clearHistory()
}
