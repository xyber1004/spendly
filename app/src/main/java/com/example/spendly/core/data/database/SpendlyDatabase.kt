package com.example.spendly.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spendly.core.data.dao.ExpenseDao
import com.example.spendly.core.data.entity.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = true)
abstract class SpendlyDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        fun build(context: Context): SpendlyDatabase =
            Room.databaseBuilder(
                context,
                SpendlyDatabase::class.java,
                "spendly.db"
            ).build()
    }
}