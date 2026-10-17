package com.example.spendly.feature.add_expense.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spendly.core.domain.enums.Category
import com.example.spendly.feature.add_expense.viewmodel.AddExpenseUiState
import com.example.spendly.feature.add_expense.viewmodel.AddExpenseViewModel
import com.example.spendly.ui.common.friendlyDate
import com.example.spendly.ui.common.style
import com.example.spendly.ui.theme.DisabledText
import com.example.spendly.ui.theme.SpendlyTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun AddExpenseRoute(
    onDone: () -> Unit,
    viewModel: AddExpenseViewModel = viewModel(factory = AddExpenseViewModel.Factory)
) {
   val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onDone()
    }

    AddExpenseScreen(
        state = state,
        onBack = onDone,
        onAmountChange = viewModel::onAmountChange,
        onCategorySelected = viewModel::onCategorySelected,
        onNoteChange = viewModel::onNoteChange,
        onDateSelected = viewModel::onDateSelected,
        onSave = viewModel::save,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    state: AddExpenseUiState,
    onBack: () -> Unit,
    onAmountChange: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onNoteChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New expense", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = onSave,
                    enabled = state.canSave,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.outline,
                        disabledContentColor = DisabledText
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Save expense", style = MaterialTheme.typography.labelLarge)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(22.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            AmountField(
                text = state.amountText,
                showError = state.showAmountError,
                onChange = onAmountChange
            )
            CategoryGrid(selected = state.category, onSelect = onCategorySelected)
            NoteField(note = state.note, onChange = onNoteChange)
            DateField(date = state.date, onDateSelected = onDateSelected)
        }
    }
}

@Composable
private fun AmountField(
    text: String,
    showError: Boolean,
    onChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val accent = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val amountStyle = MaterialTheme.typography.displayLarge

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            "Amount",
            style = MaterialTheme.typography.bodyMedium,
            color = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "₹",
                style = amountStyle.copy(fontWeight = FontWeight.SemiBold, fontSize = 36.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .alignByBaseline()
                    .padding(end = 6.dp)
            )
            BasicTextField(
                value = text,
                onValueChange = onChange,
                singleLine = true,
                textStyle = amountStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(accent),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (text.isEmpty()) {
                            Text("0", style = amountStyle, color = MaterialTheme.colorScheme.outline)
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .alignByBaseline()
                    .width(IntrinsicSize.Min)
                    .widthIn(min = 40.dp)
                    .focusRequester(focusRequester)
                    .semantics { contentDescription = "Amount in rupees" }
            )
        }
        Box(
            Modifier
                .width(200.dp)
                .height(2.dp)
                .background(accent)
        )
        if (showError) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Rounded.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Text("Enter an amount above ₹0", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
private fun CategoryGrid(selected: Category?, onSelect: (Category) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Category", style = MaterialTheme.typography.titleSmall)
        Category.entries.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { category ->
                    CategoryTile(
                        category = category,
                        selected = category == selected,
                        onClick = { onSelect(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowItems.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun CategoryTile(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val style = category.style
    Surface(
        selected = selected,
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) style.container else MaterialTheme.colorScheme.surface,
        border = if (selected) {
            BorderStroke(2.dp, style.tint)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        },
        modifier = modifier.height(76.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
        ) {
            Icon(style.icon, contentDescription = null, tint = style.tint, modifier = Modifier.size(22.dp))
            Text(
                category.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (selected) style.tint else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun NoteField(note: String, onChange: (String) -> Unit) {
    Text("Note (optional)", style = MaterialTheme.typography.titleSmall)
    OutlinedTextField(
        value = note,
        onValueChange = onChange,
        placeholder = { Text("What was it for?") },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateField(date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    var showPicker by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Date", style = MaterialTheme.typography.titleSmall)
        Surface(
            onClick = { showPicker = true },
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(friendlyDate(date, separator = ", "), style = MaterialTheme.typography.bodyLarge)
                Icon(
                    Icons.Rounded.CalendarToday,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (showPicker) {
        val todayUtcMillis = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= todayUtcMillis
            }
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                    }
                    showPicker = false
                }) { Text("Ok") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}


/// ================= PREVIEWS ================= ///

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AddExpenseFilledPreview() {
    SpendlyTheme {
        AddExpenseScreen(
            state = AddExpenseUiState(amountText = "420", category = Category.FOOD, note = "Lunch at Meghana's"),
            onBack = {}, onAmountChange = {}, onCategorySelected = {},
            onNoteChange = {}, onDateSelected = {}, onSave = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AddExpenseErrorPreview() {
    SpendlyTheme {
        AddExpenseScreen(
            state = AddExpenseUiState(amountText = "0"),
            onBack = {}, onAmountChange = {}, onCategorySelected = {},
            onNoteChange = {}, onDateSelected = {}, onSave = {},
        )
    }
}