package com.mirodeon.ev_and4.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDate

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long = 0,
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "value")
    val value: Float,
    @ColumnInfo(name = "type_id")
    val typeExpenseId: Long
)

@Entity(indices = [Index(value = ["name"], unique = true)])
data class ExpenseType(
    @PrimaryKey(autoGenerate = true)
    val typeId: Long = 0,
    @ColumnInfo(name = "name")
    val name: String
)

data class ExpenseWithType(
    @Embedded
    val expense: Expense,
    @Relation(
        parentColumn = "type_id",
        entityColumn = "typeId",
    )
    val type: ExpenseType
)