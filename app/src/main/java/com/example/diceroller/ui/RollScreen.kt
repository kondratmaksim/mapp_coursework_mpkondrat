package com.example.diceroller.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RollScreen(
    viewModel: DiceRollViewModel,
    onNavigateToHistory: () -> Unit
) {
    val uiState by viewModel.uiState
    val diceTypes = listOf(4, 6, 8, 10, 12, 20)
    val diceCounts = (1..10).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Dice Roller", style = MaterialTheme.typography.headlineLarge)

        Text(text = "Select Dice Type", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(diceTypes) { type ->
                FilterChip(
                    selected = uiState.selectedDiceType == type,
                    onClick = { viewModel.updateDiceType(type) },
                    label = { Text("d$type") }
                )
            }
        }

        Text(text = "Number of Dice", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(diceCounts) { count ->
                FilterChip(
                    selected = uiState.diceCount == count,
                    onClick = { viewModel.updateDiceCount(count) },
                    label = { Text("$count") }
                )
            }
        }

        Button(
            onClick = { viewModel.rollDice() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Roll")
        }

        if (uiState.lastRollResults.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (uiState.diceCount > 1) {
                        Text(
                            text = "Results: ${uiState.lastRollResults.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Text(
                        text = "Sum: ${uiState.lastRollSum}",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onNavigateToHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View History")
        }
    }
}
