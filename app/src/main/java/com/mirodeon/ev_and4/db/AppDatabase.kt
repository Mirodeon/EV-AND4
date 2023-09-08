package com.mirodeon.ev_and4.db

import android.content.Context
import androidx.lifecycle.coroutineScope
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mirodeon.ev_and4.converter.Converters
import com.mirodeon.ev_and4.dao.ExpenseDao
import com.mirodeon.ev_and4.dao.ExpenseTypeDao
import com.mirodeon.ev_and4.entity.Expense
import com.mirodeon.ev_and4.entity.ExpenseType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@Database(entities = [Expense::class, ExpenseType::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun expenseTypeDao(): ExpenseTypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                CoroutineScope(IO).launch {
                    if (instance.expenseTypeDao().getAll().isEmpty()) {
                        instance.expenseTypeDao().insertAll(
                            ExpenseType(name = "Tax"),
                            ExpenseType(name = "Food"),
                            ExpenseType(name = "House"),
                            ExpenseType(name = "Hobby"),
                            ExpenseType(name = "Clothes"),
                        )
                    }
                }

                INSTANCE = instance

                instance
            }
        }
    }
}