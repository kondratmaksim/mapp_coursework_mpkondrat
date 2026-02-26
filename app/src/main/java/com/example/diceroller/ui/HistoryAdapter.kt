package com.example.diceroller.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diceroller.data.DiceRoll
import com.example.diceroller.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val onClick: (DiceRoll) -> Unit) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    var items: List<DiceRoll> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    inner class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            tvTitle.text = "${item.count}d${item.diceType}"
            tvResults.text = "Results: ${item.results}"
            tvSum.text = "Sum: ${item.sum}"
            tvDate.text = dateFormat.format(Date(item.createdAt))
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun getItemCount(): Int = items.size
}
