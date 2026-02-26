package com.example.diceroller.ui

import android.app.Application
import androidx.compose.foundation.text2.input.insert
import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.diceroller.data.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceRollViewModel(application: Application) : androidx.lifecycle.AndroidViewModel(application) {
    private val repository: DiceRollRepository

    init {
        val dao = AppDatabase.getDatabase(application).diceRollDao()
        repository = DiceRollRepository(dao)
    }

    // История бросков
    val history: LiveData<List<DiceRoll>> = repository.allRolls.asLiveData()

    // Текущее состояние (тип кубика, кол-во, результат)
    val selectedDiceType = androidx.lifecycle.MutableLiveData(6)
    val diceCount = androidx.lifecycle.MutableLiveData(1)

    private val _lastRoll = androidx.lifecycle.MutableLiveData<DiceRoll?>()
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