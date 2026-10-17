package com.example.spendly.feature.expenses

import com.example.spendly.core.domain.enums.Category
import com.example.spendly.core.domain.models.Expense
import java.time.LocalDate
import java.time.YearMonth

data class ExpensesUiState (
    val month: YearMonth = YearMonth.now(),
    val selectedCategory: Category? = null,
    val monthTotalToPaise: Long = 0,
    val shownTotalInPaise: Long = 0,
    val shownCount: Int = 0,
    val breakdown: List<CategoryTotal> = emptyList(),
    val days: List<DaySection> = emptyList(),
    val isLoading: Boolean = true
)

data class CategoryTotal(val category: Category, val totalPaise: Long)

data class DaySection(
    val date: LocalDate,
    val totalPaise: Long,
    val expenses: List<Expense>
)