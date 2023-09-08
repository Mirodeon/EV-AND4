package com.mirodeon.ev_and4.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mirodeon.ev_and4.entity.Expense
import com.mirodeon.ev_and4.entity.ExpenseType
import com.mirodeon.ev_and4.entity.ExpenseWithType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Transaction
    @Query("SELECT * FROM expense ORDER BY date DESC")
    fun getAll(): Flow<List<ExpenseWithType>>

    @Transaction
    @Query("SELECT * FROM expense WHERE expenseId = :id")
    fun findById(id: Long): ExpenseWithType

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg expenses: Expense)

    fun insertAllWithType(vararg expenses: ExpenseWithType) {
        for (expense in expenses) {
            insert(expense.expense)
        }
    }

    fun deleteAllWithType(vararg expenses: ExpenseWithType) {
        for (expense in expenses) {
            delete(expense.expense)
        }
    }

    @Delete
    fun delete(expense: Expense)
}

@Dao
interface ExpenseTypeDao {
    @Query("SELECT * FROM expenseType")
    fun getAll(): List<ExpenseType>

    @Query("SELECT * FROM expenseType WHERE name = :name")
    fun findByName(name: String): ExpenseType

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(type: ExpenseType): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg genres: ExpenseType)
}