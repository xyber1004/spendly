package com.example.spendly.feature.expenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.spendly.SpendlyApp
import com.example.spendly.core.data.repository.ExpenseRepository
import com.example.spendly.core.domain.enums.Category
import com.example.spendly.core.domain.models.Expense
import com.example.spendly.feature.expenses.CategoryTotal
import com.example.spendly.feature.expenses.DaySection
import com.example.spendly.feature.expenses.ExpensesUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class ExpensesViewModel(
    private val repository: ExpenseRepository,
    private val zone: ZoneId = ZoneId.systemDefault()
) : ViewModel() {

    private val selectedMonth = MutableStateFlow(YearMonth.now(zone))
    private val selectedCategory = MutableStateFlow<Category?>(null)

    private val monthExpenses: Flow<Pair<YearMonth, List<Expense>>>
        = selectedMonth.flatMapLatest { month ->
            repository.observeMonth(month).map { expenses -> month to expenses }
        }

    val uiState: StateFlow<ExpensesUiState> =
        combine(monthExpenses, selectedCategory) { (month, expenses), category ->
            buildState(month, expenses, category)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ExpensesUiState(month = selectedMonth.value)
        )

    fun selectMonth(month: YearMonth) {
        selectedMonth.value = month
    }

    fun selectCategory(category: Category?) {
        selectedCategory.value = category
    }

    fun delete(expense: Expense) {
        viewModelScope.launch { repository.delete(expense) }
    }

    fun restore(expense: Expense) {
        viewModelScope.launch { repository.save(expense) }
    }

    private fun buildState(
        month: YearMonth,
        all: List<Expense>,
        category: Category?,
    ): ExpensesUiState {

        val shown = if (category == null) all else all.filter { it.category == category }

        val breakdown = all
            .groupBy { it.category }
            .map { (cat, items) -> CategoryTotal(cat, items.sumOf { it.amountPaise }) }
            .sortedByDescending { it.totalPaise }

        val days = shown
            .groupBy { it.spentAt.atZone(zone).toLocalDate() }
            .map { (date, items) -> DaySection(date, items.sumOf { it.amountPaise }, items) }

        return ExpensesUiState(
            month = month,
            selectedCategory = category,
            monthTotalToPaise = all.sumOf { it.amountPaise },
            shownTotalInPaise = shown.sumOf { it.amountPaise },
            shownCount = shown.size,
            breakdown = breakdown,
            days = days,
            isLoading = false
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SpendlyApp
                ExpensesViewModel(app.container.expenseRepository)
            }
        }
    }
}