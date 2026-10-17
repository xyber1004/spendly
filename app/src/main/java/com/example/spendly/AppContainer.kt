package com.example.spendly

import android.content.Context
import com.example.spendly.core.data.database.SpendlyDatabase
import com.example.spendly.core.data.repository.ExpenseRepository
import com.example.spendly.core.data.repository.RoomExpenseRepository

class AppContainer(context: Context) {

    private val database: SpendlyDatabase by lazy { SpendlyDatabase.build(context) }

    val expenseRepository: ExpenseRepository by lazy {
        RoomExpenseRepository(database.expenseDao())
    }
}