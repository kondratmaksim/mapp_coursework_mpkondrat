package com.example.diceroller.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.diceroller.R
import com.example.diceroller.databinding.FragmentRollBinding

class RollFragment : Fragment(R.layout.fragment_roll) {
    private val viewModel: DiceRollViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentRollBinding.bind(view)

        val types = listOf(4, 6, 8, 10, 12, 20)
        binding.spinnerDiceType.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, types)

        val counts = (1..10).toList()
        binding.spinnerDiceCount.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, counts)

        binding.btnRoll.setOnClickListener {
            viewModel.selectedDiceType.value = binding.spinnerDiceType.selectedItem as Int
            viewModel.diceCount.value = binding.spinnerDiceCount.selectedItem as Int
            viewModel.rollDice()
        }

        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_rollFragment_to_historyFragment)
        }

        viewModel.lastRoll.observe(viewLifecycleOwner) { roll ->
            if (roll != null) {
                binding.resultCard.visibility = View.VISIBLE
                binding.tvIndividualResults.text = "Results: ${roll.results}"
                binding.tvSum.text = roll.sum.toString()
                
                // Update spinners to match restored roll
                val typeIndex = types.indexOf(roll.diceType)
                if (typeIndex >= 0) binding.spinnerDiceType.setSelection(typeIndex)
                
                val countIndex = counts.indexOf(roll.count)
                if (countIndex >= 0) binding.spinnerDiceCount.setSelection(countIndex)
            }
        }
    }
}
