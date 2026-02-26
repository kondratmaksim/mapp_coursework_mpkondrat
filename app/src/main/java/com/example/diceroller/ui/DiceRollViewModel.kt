package com.example.diceroller.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.diceroller.data.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceRollViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DiceRollRepository

    init {
        val dao = AppDatabase.getDatabase(application).diceRollDao()
        repository = DiceRollRepository(dao)
    }

    val history: LiveData<List<DiceRoll>> = repository.allRolls.asLiveData()

    val selectedDiceType = MutableLiveData<Int>(6)
    val diceCount = MutableLiveData<Int>(1)

    private val _lastRoll = MutableLiveData<DiceRoll?>()
    val lastRoll: LiveData<DiceRoll?> = _lastRoll

    fun rollDice() {
        val type = selectedDiceType.value ?: 6
        val count = diceCount.value ?: 1
        val resultsList = List(count) { Random.nextInt(1, type + 1) }
        val sum = resultsList.sum()

        val roll = DiceRoll(
            diceType = type,
            count = count,
            results = resultsList.joinToString(", "),
            sum = sum
        )

        _lastRoll.value = roll
        viewModelScope.launch { repository.insert(roll) }
    }

    fun restoreRoll(roll: DiceRoll) {
        selectedDiceType.value = roll.diceType
        diceCount.value = roll.count
        _lastRoll.value = roll
    }

    fun clearHistory() {
        viewModelScope.launch { repository.clearHistory() }
    }
}
