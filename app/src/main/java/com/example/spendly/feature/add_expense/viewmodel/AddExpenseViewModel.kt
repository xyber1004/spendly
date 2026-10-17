package com.example.spendly.feature.add_expense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.spendly.SpendlyApp
import com.example.spendly.core.data.repository.ExpenseRepository
import com.example.spendly.core.domain.enums.Category
import com.example.spendly.core.domain.models.Expense
import com.example.spendly.core.domain.models.Money
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime


data class AddExpenseUiState(
    val amountText: String = "",
    val category: Category? = null,
    val note: String = "",
    val date: LocalDate = LocalDate.now(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
) {
    val amountPaise: Long? get() = Money.parseToPaise(amountText)
    val isAmountValid: Boolean get() = (amountPaise ?: 0L) > 0L

    val showAmountError: Boolean get() = amountText.isEmpty() && !isAmountValid

    val canSave: Boolean get() = isAmountValid && category != null && !isSaving
}

class AddExpenseViewModel(
    private val repository: ExpenseRepository,
    private val clock: Clock = Clock.systemDefaultZone()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState(date = LocalDate.now(clock)))
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    fun onAmountChange(text: String) {
        if (!Money.isAcceptableInput(text)) return
        _uiState.update { it.copy(amountText = text) }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(category = category) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(date = date) }
    }

    fun save() {
        val state = _uiState.value
        val paise = state.amountPaise
        val category = state.category
        if (!state.canSave || paise == null || category == null) return

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val spentAt = state.date
                .atTime(LocalTime.now(clock))
                .atZone(clock.zone)
                .toInstant()

            repository.save(
                Expense(
                    amountPaise = paise,
                    category = category,
                    note = state.note.trim(),
                    spentAt = spentAt
                )
            )

            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SpendlyApp
                AddExpenseViewModel(app.container.expenseRepository)
            }
        }
    }
}