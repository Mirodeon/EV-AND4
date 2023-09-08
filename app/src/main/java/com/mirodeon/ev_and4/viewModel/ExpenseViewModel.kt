package com.mirodeon.ev_and4.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mirodeon.ev_and4.dao.ExpenseDao
import com.mirodeon.ev_and4.entity.Expense
import com.mirodeon.ev_and4.entity.ExpenseType
import com.mirodeon.ev_and4.entity.ExpenseWithType
import kotlinx.coroutines.flow.Flow

class ExpenseViewModel(private val expenseDao: ExpenseDao) : ViewModel() {

    fun fullExpense(): Flow<List<ExpenseWithType>> = expenseDao.getAll()

    fun insertExpense(vararg expenses: Expense) = expenseDao.insertAll(*expenses)

}

class ExpenseViewModelFactory(
    private val expenseDao: ExpenseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(expenseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}