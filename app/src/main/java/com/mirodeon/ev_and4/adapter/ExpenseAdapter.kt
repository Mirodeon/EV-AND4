package com.mirodeon.ev_and4.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.ev_and4.databinding.ExpenseItemBinding
import com.mirodeon.ev_and4.entity.ExpenseWithType

class ExpenseAdapter(
    private val onItemClicked: (ExpenseWithType) -> Unit
) : ListAdapter<ExpenseWithType, ExpenseAdapter.ExpenseViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ExpenseWithType>() {
            override fun areItemsTheSame(
                oldItem: ExpenseWithType,
                newItem: ExpenseWithType
            ): Boolean {
                return oldItem.expense.expenseId == newItem.expense.expenseId
            }

            override fun areContentsTheSame(
                oldItem: ExpenseWithType,
                newItem: ExpenseWithType
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val viewHolder = ExpenseViewHolder(
            ExpenseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(
        private var binding: ExpenseItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(expense: ExpenseWithType) {

        }
    }
}
