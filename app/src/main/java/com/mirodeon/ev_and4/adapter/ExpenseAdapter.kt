package com.mirodeon.ev_and4.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.ev_and4.R
import com.mirodeon.ev_and4.app.MyApp
import com.mirodeon.ev_and4.databinding.ExpenseItemBinding
import com.mirodeon.ev_and4.entity.ExpenseWithType
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(
        private var binding: ExpenseItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SimpleDateFormat")
        fun bind(expense: ExpenseWithType) {
            binding.typeItemText.text = expense.type.name
            binding.nameItemText.text = expense.expense.name
            binding.amountItemText.text =
                MyApp.instance.getString(R.string.to_euro, expense.expense.value.toString())
            binding.dateItemText.text =
                expense.expense.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        }
    }
}
