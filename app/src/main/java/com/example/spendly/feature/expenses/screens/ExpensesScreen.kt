package com.example.spendly.feature.expenses.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.spendly.feature.expenses.viewmodel.ExpensesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spendly.core.domain.enums.Category
import com.example.spendly.core.domain.models.Expense
import com.example.spendly.feature.expenses.ExpensesUiState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spendly.R
import com.example.spendly.core.domain.models.Money
import com.example.spendly.feature.expenses.components.CategoryFilterRow
import com.example.spendly.feature.expenses.components.DayHeader
import com.example.spendly.feature.expenses.components.EmptyState
import com.example.spendly.feature.expenses.components.ExpenseRow
import com.example.spendly.feature.expenses.components.MonthDropdownMenu
import com.example.spendly.feature.expenses.components.SummaryCard
import com.example.spendly.feature.expenses.components.SwipeToDeleteRow
import com.example.spendly.feature.expenses.components.rowShape
import kotlinx.coroutines.launch
import java.time.YearMonth

@Composable
fun ExpensesRoute (
    onAddExpense: () -> Unit,
    viewModel: ExpensesViewModel = viewModel(factory = ExpensesViewModel.Factory)
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ExpensesScreen(
        state = state,
        onMonthSelected = viewModel::selectMonth,
        onCategorySelected = viewModel::selectCategory,
        onDelete = viewModel::delete,
        onUndoDelete = viewModel::restore,
        onAddExpense = onAddExpense,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    state: ExpensesUiState,
    onMonthSelected: (YearMonth) -> Unit,
    onCategorySelected: (Category?) -> Unit,
    onDelete: (Expense) -> Unit,
    onUndoDelete: (Expense) -> Unit,
    onAddExpense: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun deleteWithUndo(expense: Expense) {
        onDelete(expense)
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = "${expense.title} - ${Money.format(expense.amountPaise)} deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        topBar = { ExpensesTopBar(month = state.month, onMonthSelected = onMonthSelected) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick =  onAddExpense,
                icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                text = { Text("Add expense", style = MaterialTheme.typography.labelLarge) },
                shape = RoundedCornerShape(18.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding() + 96.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            item(key = "summary") {
                SummaryCard(state, modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 4.dp))
            }
            item(key = "filters") {
                CategoryFilterRow(
                    selected = state.selectedCategory,
                    onSelected = onCategorySelected,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }

            if (!state.isLoading && state.days.isEmpty()) {
                item(key = "empty") {
                    val category = state.selectedCategory
                    EmptyState(
                        title = if (category == null) "No expenses yet" else "Nothing in ${category.label}",
                        message = "Tap Add expense to log one."
                    )
                }
            }

            state.days.forEach { day ->
                stickyHeader(key = "header-${day.date}") {
                    DayHeader(day)
                }
                itemsIndexed(
                    items = day.expenses,
                    key = { _, expense -> expense.id }
                ) {
                    index, expense ->
                    val shape = rowShape(index, day.expenses.size)
                    SwipeToDeleteRow(
                        onDelete = { deleteWithUndo(expense) },
                        shape = shape,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .animateItem()
                    ) {
                        ExpenseRow(expense = expense, showDivider = index < day.expenses.lastIndex)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpensesTopBar(
    month: YearMonth,
    onMonthSelected: (YearMonth) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(stringResource(R.string.spendly), style = MaterialTheme.typography.headlineSmall)
        }

        MonthDropdownMenu(selected = month, onSelect = onMonthSelected)
    }
}