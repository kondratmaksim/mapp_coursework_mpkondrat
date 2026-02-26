package com.example.diceroller.ui

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diceroller.data.AppDatabase
import com.example.diceroller.data.DiceRoll
import com.example.diceroller.data.DiceRollRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class RollUiState(
    val selectedDiceType: Int = 6,
    val diceCount: Int = 1,
    val lastRollResults: List<Int> = emptyList(),
    val lastRollSum: Int = 0
)

class DiceRollViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DiceRollRepository

    init {
        val dao = AppDatabase.getDatabase(application).diceRollDao()
        repository = DiceRollRepository(dao)
    }

    val history: StateFlow<List<DiceRoll>> = repository.allRolls.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _uiState = mutableStateOf(RollUiState())
    val uiState: State<RollUiState> = _uiState

    fun updateDiceType(type: Int) {
        _uiState.value = _uiState.value.copy(selectedDiceType = type)
    }

    fun updateDiceCount(count: Int) {
        _uiState.value = _uiState.value.copy(diceCount = count)
    }

    fun rollDice() {
        val results = List(_uiState.value.diceCount) {
            Random.nextInt(1, _uiState.value.selectedDiceType + 1)
        }
        val sum = results.sum()

        _uiState.value = _uiState.value.copy(
            lastRollResults = results,
            lastRollSum = sum
        )

        viewModelScope.launch {
            val roll = DiceRoll(
                diceType = _uiState.value.selectedDiceType,
                count = _uiState.value.diceCount,
                results = results.joinToString(","),
                sum = sum
            )
            repository.insert(roll)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun restoreRoll(roll: DiceRoll) {
        _uiState.value = _uiState.value.copy(
            selectedDiceType = roll.diceType,
            diceCount = roll.count,
            lastRollResults = roll.results.split(",").filter { it.isNotEmpty() }.map { it.toInt() },
            lastRollSum = roll.sum
        )
    }
}
