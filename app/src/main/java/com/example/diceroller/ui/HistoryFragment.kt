package com.example.diceroller.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.diceroller.R
import com.example.diceroller.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val viewModel: DiceRollViewModel by activityViewModels()
    private var adapter: HistoryAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentHistoryBinding.bind(view)

        adapter = HistoryAdapter { roll ->
            viewModel.restoreRoll(roll)
            findNavController().popBackStack()
        }
        binding.rvHistory.adapter = adapter

        viewModel.history.observe(viewLifecycleOwner) { history ->
            adapter?.items = history
            binding.tvEmpty.visibility = if (history.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }
}
